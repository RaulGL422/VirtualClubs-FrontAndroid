package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.AuthRepository
import javax.inject.Inject

/** Caso de uso para autenticar un usuario con email y contraseña. */
class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /** @return [Result] con [AuthTokens] en éxito o [VirtualClubException] en fallo. */
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}