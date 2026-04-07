package es.virtualclubs.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.virtualclubs.BuildConfig
import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.managers.SafeResponse
import es.virtualclubs.data.remote.api.AuthApi
import es.virtualclubs.data.remote.api.RefreshApi
import es.virtualclubs.data.remote.api.UserApi
import es.virtualclubs.data.repository.AuthRepositoryImpl
import es.virtualclubs.data.repository.RefreshRepositoryImpl
import es.virtualclubs.data.repository.UserRepositoryImpl
import es.virtualclubs.domain.model.AuthInterceptor
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.domain.repository.RefreshRepository
import es.virtualclubs.domain.repository.UserRepository
import es.virtualclubs.presentation.navigation.SessionManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.CertificatePinner
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
  fun provideRetrofit(secureUserPreferences: SecureUserPreferences): Retrofit {
    val logging = HttpLoggingInterceptor().apply {
      level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS
      else HttpLoggingInterceptor.Level.NONE
    }

    val clientBuilder = OkHttpClient.Builder()
      .addInterceptor(AuthInterceptor {
        runBlocking { secureUserPreferences.accessToken.firstOrNull() }
      })
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

    return Retrofit.Builder()
      .baseUrl(BuildConfig.BASE_URL)
      .client(client)
      .addConverterFactory(GsonConverterFactory.create())
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
  fun provideRefreshApi(retrofit: Retrofit): RefreshApi =
    retrofit.create(RefreshApi::class.java)

  @Provides
  @Singleton
  fun provideRefreshRepository(
    api: RefreshApi,
    sessionManager: SessionManager,
    secureUserPreferences: SecureUserPreferences
  ): RefreshRepository =
    RefreshRepositoryImpl(api, sessionManager, secureUserPreferences = secureUserPreferences)

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
  fun provideSafeCall(refresh: RefreshRepository): SafeResponse =
    SafeResponse(refresh)
}