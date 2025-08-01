package es.virtualclubs.data.repository

import es.virtualclubs.data.remote.api.AuthApi
import es.virtualclubs.data.remote.dto.AuthRequest
import es.virtualclubs.data.remote.dto.GoogleAuthRequest
import es.virtualclubs.data.remote.dto.LogoutRequest
import es.virtualclubs.data.remote.dto.RefreshRequest
import es.virtualclubs.data.remote.dto.RegisterRequest
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthTokens> {
        return try {
            val response = api.login(AuthRequest(email, password))
            if (response.success && response.data != null) {
                val tokens = AuthTokens(
                    accessToken = response.data["accessToken"] ?: "",
                    refreshToken = response.data["refreshToken"] ?: ""
                )
                Result.success(tokens)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String): Result<AuthTokens> {
        return try {
            val response = api.register(RegisterRequest(email, password))
            if (response.success && response.data != null) {
                val tokens = AuthTokens(
                    accessToken = response.data["accessToken"] ?: "",
                    refreshToken = response.data["refreshToken"] ?: ""
                )
                Result.success(tokens)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refresh(refreshToken: String): Result<AuthTokens> {
        return try {
            val response = api.refresh(RefreshRequest(refreshToken))
            if (response.success && response.data != null) {
                val tokens = AuthTokens(
                    accessToken = response.data["accessToken"] ?: "",
                    refreshToken = response.data["refreshToken"] ?: ""
                )
                Result.success(tokens)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(refreshToken: String): Result<Unit> {
        return try {
            val response = api.logout(LogoutRequest(refreshToken))
            if (response.success) Result.success(Unit)
            else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun google(idToken: String) : Result<AuthTokens> {
        return try {
            val response = api.google(GoogleAuthRequest(idToken))
            if (response.success && response.data != null) {
                val tokens = AuthTokens(
                    accessToken = response.data["accessToken"] ?: "",
                    refreshToken = response.data["refreshToken"] ?: ""
                )
                Result.success(tokens)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}