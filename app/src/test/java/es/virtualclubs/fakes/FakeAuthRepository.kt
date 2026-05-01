package es.virtualclubs.fakes

import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.repository.AuthRepository

/**
 * Implementación fake de [AuthRepository] para tests unitarios.
 *
 * Permite configurar el resultado de cada operación antes de ejecutar el test,
 * e inspeccionar qué argumentos se pasaron a cada llamada.
 *
 * Preferir este fake sobre mockk para tests de use cases y ViewModels,
 * ya que produce tests más legibles y sin boilerplate de `coEvery`.
 */
class FakeAuthRepository : AuthRepository {

    // ─── Resultados configurables ─────────────────────────────────────────────

    var loginResult: Result<AuthTokens> = Result.success(AuthTokens("access", "refresh"))
    var registerResult: Result<AuthTokens> = Result.success(AuthTokens("access", "refresh"))
    var logoutResult: Result<Unit> = Result.success(Unit)
    var googleResult: Result<AuthTokens> = Result.success(AuthTokens("access", "refresh"))
    var requestPasswordResetResult: Result<Unit> = Result.success(Unit)
    var resetPasswordResult: Result<Unit> = Result.success(Unit)
    var requestVerifyResult: Result<Unit> = Result.success(Unit)

    // ─── Captura de argumentos ────────────────────────────────────────────────

    var lastLoginEmail: String? = null
    var lastLoginPassword: String? = null
    var lastRegisterEmail: String? = null
    var lastRegisterPassword: String? = null
    var lastGoogleIdToken: String? = null
    var lastResetPasswordToken: String? = null
    var lastNewPassword: String? = null
    var lastRequestPasswordResetEmail: String? = null

    // ─── Contadores de invocaciones ───────────────────────────────────────────

    var loginCallCount = 0
    var logoutCallCount = 0
    var requestVerifyCallCount = 0

    // ─── Implementación ───────────────────────────────────────────────────────

    override suspend fun login(email: String, password: String): Result<AuthTokens> {
        loginCallCount++
        lastLoginEmail = email
        lastLoginPassword = password
        return loginResult
    }

    override suspend fun register(email: String, password: String): Result<AuthTokens> {
        lastRegisterEmail = email
        lastRegisterPassword = password
        return registerResult
    }

    override suspend fun logout(): Result<Unit> {
        logoutCallCount++
        return logoutResult
    }

    override suspend fun requestPasswordReset(email: String): Result<Unit> {
        lastRequestPasswordResetEmail = email
        return requestPasswordResetResult
    }

    override suspend fun google(idToken: String): Result<AuthTokens> {
        lastGoogleIdToken = idToken
        return googleResult
    }

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> {
        lastResetPasswordToken = token
        lastNewPassword = newPassword
        return resetPasswordResult
    }

    override suspend fun requestVerify(): Result<Unit> {
        requestVerifyCallCount++
        return requestVerifyResult
    }
}
