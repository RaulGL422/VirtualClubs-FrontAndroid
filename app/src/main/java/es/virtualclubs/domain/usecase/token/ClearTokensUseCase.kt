package es.virtualclubs.domain.usecase.token

import es.virtualclubs.data.local.secure.SecureUserPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClearTokensUseCase @Inject constructor(
    private val securePrefs: SecureUserPreferences
) {
    suspend operator fun invoke() {
        securePrefs.clearAll()
    }
}
