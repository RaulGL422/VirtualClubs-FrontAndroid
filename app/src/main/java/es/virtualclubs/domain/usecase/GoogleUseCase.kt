package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.AuthRepository
import javax.inject.Inject

/** Caso de uso para autenticar un usuario con Google (ID token de CredentialManager). */
class GoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /** @param idToken Token de identidad obtenido de [GetGoogleIdOption]. */
    suspend operator fun invoke(idToken: String) = repository.google(idToken)
}