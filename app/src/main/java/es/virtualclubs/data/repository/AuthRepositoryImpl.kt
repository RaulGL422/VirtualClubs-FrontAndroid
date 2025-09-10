package es.virtualclubs.data.repository

import com.google.gson.Gson
import es.virtualclubs.data.remote.api.AuthApi
import es.virtualclubs.data.remote.dto.*
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface TokenRefresher {
    suspend fun refreshToken(token: String) : Result<AuthTokens>
}

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val safeCall: SafeCall
) : AuthRepository, TokenRefresher {

    override suspend fun login(email: String, password: String): Result<AuthTokens> =
        safeAuthCall { api.login(AuthRequest(email, password)) }

    override suspend fun register(email: String, password: String): Result<AuthTokens> =
        safeAuthCall { api.register(RegisterRequest(email, password)) }

    override suspend fun refresh(refreshToken: String): Result<AuthTokens> =
        safeAuthCall { api.refresh(RefreshRequest(refreshToken)) }

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

    override suspend fun refreshToken(token: String) : Result<AuthTokens> {
        return refresh(token)
    }
}