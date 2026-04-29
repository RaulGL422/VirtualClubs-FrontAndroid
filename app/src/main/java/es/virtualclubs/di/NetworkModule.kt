package es.virtualclubs.di

import com.google.gson.Gson
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.virtualclubs.BuildConfig
import es.virtualclubs.data.local.datastore.AppPreferences
import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.managers.SafeResponse
import es.virtualclubs.data.remote.api.AuthApi
import es.virtualclubs.data.remote.api.UserApi
import es.virtualclubs.data.repository.AuthRepositoryImpl
import es.virtualclubs.data.repository.RefreshRepositoryImpl
import es.virtualclubs.data.repository.UserRepositoryImpl
import es.virtualclubs.domain.model.AuthInterceptor
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.domain.repository.RefreshRepository
import es.virtualclubs.domain.repository.UserRepository
import es.virtualclubs.data.session.UserSession
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import okhttp3.CertificatePinner
import java.util.concurrent.atomic.AtomicBoolean
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun provideGson(): Gson = Gson()

  @Provides
  @Singleton
  fun provideRetrofit(
    userSession: UserSession,
    refreshRepository: Lazy<RefreshRepository>,
    gson: Gson,
    appPreferences: AppPreferences
  ): Retrofit {
    val logging = HttpLoggingInterceptor().apply {
      level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS
      else HttpLoggingInterceptor.Level.NONE
    }

    // AtomicBoolean evita que múltiples hilos de OkHttp lancen refreshes simultáneos.
    // runBlocking es intencional: AuthInterceptor es un Interceptor síncrono de OkHttp
    // que corre en el hilo de red (no el principal), por lo que no puede causar ANR.
    val isProactivelyRefreshing = AtomicBoolean(false)

    val clientBuilder = OkHttpClient.Builder()
      .addInterceptor(AuthInterceptor(
        tokenProvider = { userSession.cachedAccessToken },
        onTokenExpired = {
          if (isProactivelyRefreshing.compareAndSet(false, true)) {
            try { runBlocking { refreshRepository.get().refresh() } }
            finally { isProactivelyRefreshing.set(false) }
          }
        }
      ))
      .addInterceptor(logging)

    // Certificate pinning solo en prod para proteger contra MITM
    // Pins: CA intermedio (Google Trust Services WE1) + Root CA (GTS Root R4)
    // Actualizar cuando Render cambie de CA. Ver sección "Certificate Pinning" en CLAUDE.md
    if (BuildConfig.FLAVOR == "prod") {
      clientBuilder.certificatePinner(
        CertificatePinner.Builder()
          .add("virtualclubs-backend.onrender.com", "sha256/kIdp6NNEd8wsugYyyIYFsi1ylMCED3hZbSR8ZFsa/A4=")
          .add("virtualclubs-backend.onrender.com", "sha256/mEflZT5enoR1FuXLgYYGqnVEoZvmf9c2bVBpiOjYQ0c=")
          .build()
      )
    }

    val client = clientBuilder.build()

    val baseUrl = if (BuildConfig.DEBUG) {
      val saved = runBlocking { appPreferences.debugServerUrlFlow.first() }.trim()
      if (saved.isBlank()) {
        BuildConfig.BASE_URL
      } else {
        var url = saved
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://$url"
        if (!url.endsWith("/")) url = "$url/"
        url
      }
    } else {
      BuildConfig.BASE_URL
    }

    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(client)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build()
  }

  @Provides
  @Singleton
  fun provideAuthApi(retrofit: Retrofit): AuthApi =
    retrofit.create(AuthApi::class.java)

  @Provides
  @Singleton
  fun provideAuthRepository(api: AuthApi, safeResponse: SafeResponse): AuthRepository =
    AuthRepositoryImpl(api, safeResponse)

  @Provides
  @Singleton
  fun provideRefreshRepository(
    api: AuthApi,
    secureUserPreferences: SecureUserPreferences,
    userSession: UserSession
  ): RefreshRepository =
    RefreshRepositoryImpl(api, secureUserPreferences, userSession)

  @Provides
  @Singleton
  fun provideUserApi(retrofit: Retrofit): UserApi =
    retrofit.create(UserApi::class.java)

  @Provides
  @Singleton
  fun provideUserRepository(
    api: UserApi,
    safeResponse: SafeResponse
  ): UserRepository =
    UserRepositoryImpl(api, safeResponse)

  @Provides
  @Singleton
  fun provideSafeCall(refresh: RefreshRepository, gson: Gson): SafeResponse =
    SafeResponse(refresh, gson)
}
