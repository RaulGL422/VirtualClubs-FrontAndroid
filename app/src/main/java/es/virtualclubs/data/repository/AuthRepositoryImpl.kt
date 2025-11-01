package es.virtualclubs.data.repository

import es.virtualclubs.data.remote.api.AuthApi
import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.data.remote.dto.AuthRequest
import es.virtualclubs.data.remote.dto.GoogleAuthRequest
import es.virtualclubs.data.remote.dto.RegisterRequest
import es.virtualclubs.data.remote.dto.RequestPasswordResetRequest
import es.virtualclubs.data.remote.dto.ResetPasswordRequest
import es.virtualclubs.data.remote.dto.getOrThrow
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

  override suspend fun logout(): Result<Unit> =
    safeCall.safeCall {
      val result = api.logout().getOrThrow()
      Result.success(result)
    }

  override suspend fun google(idToken: String): Result<AuthTokens> =
    safeAuthCall { api.google(GoogleAuthRequest(idToken)) }

  override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> =
    safeCall.safeCall {
      val result = api.resetPassword(ResetPasswordRequest(token, newPassword)).getOrThrow()
      Result.success(result)
    }

  override suspend fun requestPasswordReset(email: String): Result<Unit> =
    safeCall.safeCall {
      val result = api.requestPasswordReset(RequestPasswordResetRequest(email)).getOrThrow()
      Result.success(result)
    }

  override suspend fun requestVerify(): Result<Unit> =
    safeCall.safeCall {
      val result = api.requestVerify().getOrThrow()
      Result.success(result)
    }

  // --- PRIVATE HELPERS ---

  private suspend fun safeAuthCall(
    block: suspend () -> ApiResponse<AuthTokens>
  ): Result<AuthTokens> = safeCall.safeCall {
    val result = block().getOrThrow()
    Result.success(result)
  }
}