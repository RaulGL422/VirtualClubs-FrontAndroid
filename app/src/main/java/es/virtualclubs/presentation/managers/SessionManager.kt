package es.virtualclubs.presentation.managers

import es.virtualclubs.domain.usecase.LogoutUserUseCase
import es.virtualclubs.presentation.navigation.AppNavigator
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase,
    private val appNavigator: AppNavigator
) {
    suspend fun logout() {
        logoutUserUseCase(notifyBackend = false)
        appNavigator.navigateToLoginAndClearStack()
    }
}
