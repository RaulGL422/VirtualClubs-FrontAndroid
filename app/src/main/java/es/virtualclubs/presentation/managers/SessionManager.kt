package es.virtualclubs.presentation.managers

import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.presentation.navigation.AppNavigator
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val userPreferences: UserPreferences,
    private val secureUserPreferences: SecureUserPreferences,
    private val userSession: UserSession,
    private val appNavigator: AppNavigator
) {
    suspend fun logout() {
        userPreferences.clearUser()
        secureUserPreferences.clearAll()
        userSession.logout()
        appNavigator.navigateToLoginAndClearStack()
    }
}
