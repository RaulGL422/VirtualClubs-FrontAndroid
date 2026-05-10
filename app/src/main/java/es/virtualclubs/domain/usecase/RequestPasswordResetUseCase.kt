package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestPasswordResetUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String) = repository.requestPasswordReset(email)
}
