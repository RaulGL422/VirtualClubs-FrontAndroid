package es.virtualclubs.domain.usecase.token

import es.virtualclubs.data.local.secure.SecureUserPreferences
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRefreshTokenUseCase @Inject constructor(
    private val securePrefs: SecureUserPreferences
) {
    suspend operator fun invoke(): String? {
        return securePrefs.refreshToken.firstOrNull()
    }
}
