package es.virtualclubs.presentation.navigation

import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.domain.usecase.LogoutUserUseCaseFactory
import jakarta.inject.Inject
import jakarta.inject.Provider

class SessionManager @Inject constructor(
    private val logoutFactory: Provider<LogoutUserUseCaseFactory>,
    private val userPreferences: UserPreferences,
    private val appNavigator: AppNavigatorImpl
) {
    suspend fun logout(token: String) {
        logoutFactory.get().create().invoke(token)
        userPreferences.clearUser()
        appNavigator.navigateToLoginAndClearStack()
    }
}