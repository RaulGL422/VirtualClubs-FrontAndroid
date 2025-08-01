package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.AuthRepository
import jakarta.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(refreshToken: String) = repository.logout(refreshToken)
}