package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}