package es.virtualclubs.domain.usecase

import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.domain.usecase.token.ClearTokensUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutUserUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val userSession: UserSession,
    private val clearTokensUseCase: ClearTokensUseCase,
    private val userPreferences: UserPreferences
) {
    suspend operator fun invoke(notifyBackend: Boolean = true) {
        if (notifyBackend) runCatching { repository.logout() }
        userSession.logout()
        clearTokensUseCase()
        userPreferences.clearUser()
    }
}
