package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.AuthRepository
import jakarta.inject.Inject

class GoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String) = repository.google(idToken)
}