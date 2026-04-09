package es.virtualclubs.presentation.navigation

import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.local.secure.SecureUserPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(
  private val userPreferences: UserPreferences,
  private val secureUserPreferences: SecureUserPreferences
) {
  suspend fun logout() {
    userPreferences.clearUser()
    secureUserPreferences.clearAll()
    AppNavigator.navigateToLoginAndClearStack()
  }
}