package es.virtualclubs.domain.usecase

import es.virtualclubs.data.repository.TokenRefresher
import es.virtualclubs.domain.repository.AuthRepository
import jakarta.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val refresher: TokenRefresher
) {
    suspend operator fun invoke(token: String) = refresher.refreshToken(token)
}