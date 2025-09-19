package es.virtualclubs.data.repository

import es.virtualclubs.data.remote.api.AuthApi
import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.data.remote.dto.AuthRequest
import es.virtualclubs.data.remote.dto.GoogleAuthRequest
import es.virtualclubs.data.remote.dto.LogoutRequest
import es.virtualclubs.data.remote.dto.RegisterRequest
import es.virtualclubs.data.remote.dto.RequestPasswordResetRequest
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val safeCall: SafeCall
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthTokens> =
        safeAuthCall { api.login(AuthRequest(email, password)) }

    override suspend fun register(email: String, password: String): Result<AuthTokens> =
        safeAuthCall { api.register(RegisterRequest(email, password)) }

    override suspend fun logout(refreshToken: String): Result<Unit> =
        safeCall.safeCall {
            val response = api.logout(LogoutRequest(refreshToken))
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        }

    override suspend fun google(idToken: String): Result<AuthTokens> =
        safeAuthCall { api.google(GoogleAuthRequest(idToken)) }

    override suspend fun requestPasswordReset(email: String): Result<Unit> =
        safeCall.safeCall {
            val response = api.requestPasswordReset(RequestPasswordResetRequest(email))
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        }

    // --- PRIVATE HELPERS ---

    private suspend fun safeAuthCall(
        block: suspend () -> ApiResponse<Map<String, String>>
    ): Result<AuthTokens> = safeCall.safeCall {
        val response = block()
        if (response.success && response.data != null) {
            val tokens = AuthTokens(
                accessToken = response.data["accessToken"] ?: "",
                refreshToken = response.data["refreshToken"] ?: ""
            )
            Result.success(tokens)
        } else {
            Result.failure(Exception(response.message))
        }
    }
}