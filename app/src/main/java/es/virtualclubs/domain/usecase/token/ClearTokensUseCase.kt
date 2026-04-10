package es.virtualclubs.domain.usecase.token

import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.session.UserSession
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClearTokensUseCase @Inject constructor(
    private val securePrefs: SecureUserPreferences,
    private val userSession: UserSession
) {
    suspend operator fun invoke() {
        securePrefs.clearAll()
        userSession.cacheAccessToken(null)
    }
}
