package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/** Caso de uso para registrar un nuevo usuario con email y contraseña. */
@Singleton
class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /** @return [Result] con [AuthTokens] en éxito o [VirtualClubException] en fallo. */
    suspend operator fun invoke(email: String, password: String) = repository.register(email, password)
}