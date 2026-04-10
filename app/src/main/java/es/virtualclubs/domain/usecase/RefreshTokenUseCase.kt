package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.RefreshRepository
import javax.inject.Inject
import javax.inject.Singleton

/** Caso de uso para refrescar el access token usando el refresh token almacenado. */
@Singleton
class RefreshTokenUseCase @Inject constructor(
    private val refresher: RefreshRepository
) {
    suspend operator fun invoke() = refresher.refresh()
}
