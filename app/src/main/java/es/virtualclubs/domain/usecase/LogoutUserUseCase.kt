package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.session.UserSession
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val userSession: UserSession
) {
    suspend operator fun invoke() {
        repository.logout()
        userSession.clearUser()
    }
}
