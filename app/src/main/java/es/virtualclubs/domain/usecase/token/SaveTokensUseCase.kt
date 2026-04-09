package es.virtualclubs.domain.usecase.token

import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.session.UserSession
import javax.inject.Inject

class SaveTokensUseCase @Inject constructor(
    private val securePrefs: SecureUserPreferences,
    private val userSession: UserSession
) {
    suspend operator fun invoke(accessToken: String, refreshToken: String) {
        securePrefs.saveAccessToken(accessToken)
        securePrefs.saveRefreshToken(refreshToken)
        userSession.cacheAccessToken(accessToken)
    }
}
