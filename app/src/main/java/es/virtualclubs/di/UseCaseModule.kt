package es.virtualclubs.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.domain.repository.RefreshRepository
import es.virtualclubs.domain.repository.UserRepository
import es.virtualclubs.domain.usecase.AuthUseCase
import es.virtualclubs.domain.usecase.GetUserInfoUseCase
import es.virtualclubs.domain.usecase.GoogleUseCase
import es.virtualclubs.domain.usecase.LogoutUserUseCase
import es.virtualclubs.domain.usecase.RefreshTokenUseCase
import es.virtualclubs.domain.usecase.RegisterUseCase
import es.virtualclubs.domain.usecase.token.ClearTokensUseCase
import es.virtualclubs.domain.usecase.token.GetAccessTokenUseCase
import es.virtualclubs.domain.usecase.token.GetRefreshTokenUseCase
import es.virtualclubs.domain.usecase.token.SaveTokensUseCase
import es.virtualclubs.session.UserSession
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideAuthUseCase(repository: AuthRepository): AuthUseCase {
        return AuthUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRefreshTokenUseCase(tokenRefresher: RefreshRepository): RefreshTokenUseCase {
        return RefreshTokenUseCase(tokenRefresher)
    }

    @Provides
    @Singleton
    fun provideLogoutUserUseCase(repository: AuthRepository): LogoutUserUseCase {
        return LogoutUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGoogleUserUseCase(repository: AuthRepository): GoogleUseCase {
        return GoogleUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideClearTokensUseCase(securePreferences: SecureUserPreferences): ClearTokensUseCase {
        return ClearTokensUseCase(securePreferences)
    }

    @Provides
    @Singleton
    fun provideGetAccessTokenUseCase(securePreferences: SecureUserPreferences): GetAccessTokenUseCase {
        return GetAccessTokenUseCase(securePreferences)
    }

    @Provides
    @Singleton
    fun provideGetRefreshTokenUseCase(securePreferences: SecureUserPreferences): GetRefreshTokenUseCase {
        return GetRefreshTokenUseCase(securePreferences)
    }

    @Provides
    @Singleton
    fun provideSaveTokensUseCase(securePreferences: SecureUserPreferences): SaveTokensUseCase {
        return SaveTokensUseCase(securePreferences)
    }

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(userRepository: UserRepository, userSession: UserSession): GetUserInfoUseCase {
        return GetUserInfoUseCase(userRepository, userSession)
    }
}