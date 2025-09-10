package es.virtualclubs.presentation.navigation

import dagger.internal.Provider
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.domain.usecase.LogoutUserUseCase
import es.virtualclubs.domain.usecase.LogoutUserUseCaseFactory
import es.virtualclubs.domain.usecase.token.GetRefreshTokenUseCase
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionManager @Inject constructor(
    private val appNavigator: AppNavigator,
    private val logoutFactory: LogoutUserUseCaseFactory
) {
    suspend fun logout(token: String) {
        logoutFactory.create().invoke(token)
        appNavigator.navigateToLoginAndClearStack()
    }
}