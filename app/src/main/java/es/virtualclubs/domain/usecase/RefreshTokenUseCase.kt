package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.RefreshRepository
import javax.inject.Inject

/** Caso de uso para refrescar el access token usando el refresh token almacenado. */
class RefreshTokenUseCase @Inject constructor(
    private val refresher: RefreshRepository
) {
    /**
     * @param canLogout Si es `true` y el refresh falla, se desloguea al usuario automáticamente.
     *                  Usar `false` en el flujo de auto-login para no forzar logout.
     */
    suspend operator fun invoke(canLogout: Boolean = true) = refresher.refresh(canLogout)
}