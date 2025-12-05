package es.virtualclubs.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.virtualclubs.BuildConfig
import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.remote.api.AuthApi
import es.virtualclubs.data.remote.api.RefreshApi
import es.virtualclubs.data.remote.api.UserApi
import es.virtualclubs.data.repository.AuthRepositoryImpl
import es.virtualclubs.data.repository.RefreshRepositoryImpl
import es.virtualclubs.data.managers.SafeResponse
import es.virtualclubs.data.repository.UserRepositoryImpl
import es.virtualclubs.domain.model.AuthInterceptor
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.domain.repository.RefreshRepository
import es.virtualclubs.domain.repository.UserRepository
import es.virtualclubs.domain.usecase.token.GetRefreshTokenUseCase
import es.virtualclubs.domain.usecase.token.SaveTokensUseCase
import es.virtualclubs.presentation.navigation.SessionManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
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
      level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
      else HttpLoggingInterceptor.Level.NONE
    }

    val client = OkHttpClient.Builder()
      .addInterceptor(logging)

    val token = runBlocking { secureUserPreferences.accessToken.firstOrNull() }
    if (token != null) client.addInterceptor(AuthInterceptor { token })

    return Retrofit.Builder()
      .baseUrl(BuildConfig.BASE_URL)
      .client(client.build())
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
    saveTokensUseCase: SaveTokensUseCase,
    refreshTokenUseCase: GetRefreshTokenUseCase
  ): RefreshRepository =
    RefreshRepositoryImpl(api, sessionManager, saveTokensUseCase, refreshTokenUseCase)

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