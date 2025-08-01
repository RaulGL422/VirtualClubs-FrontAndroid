package es.virtualclubs.domain.usecase.token

import es.virtualclubs.data.local.secure.SecureUserPreferences
import javax.inject.Inject

class SaveTokensUseCase @Inject constructor(
    private val securePrefs: SecureUserPreferences
) {
    suspend operator fun invoke(accessToken: String, refreshToken: String) {
        securePrefs.saveAccessToken(accessToken)
        securePrefs.saveRefreshToken(refreshToken)
    }
}