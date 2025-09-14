package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.RefreshRepository
import jakarta.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val refresher: RefreshRepository
) {
    suspend operator fun invoke(canLogout: Boolean = true) = refresher.refresh(canLogout)
}