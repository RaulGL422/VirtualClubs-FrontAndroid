package es.virtualclubs.data.repository

import es.virtualclubs.data.managers.SafeResponse
import es.virtualclubs.data.remote.api.AuthApi
import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.data.remote.dto.AuthRequest
import es.virtualclubs.data.remote.dto.GoogleAuthRequest
import es.virtualclubs.data.remote.dto.RegisterRequest
import es.virtualclubs.data.remote.dto.RequestPasswordResetRequest
import es.virtualclubs.data.remote.dto.ResetPasswordRequest
import es.virtualclubs.data.remote.dto.getOrThrow
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val api: AuthApi,
  private val safeResponse: SafeResponse
) : AuthRepository {
  override suspend fun login(email: String, password: String): Result<AuthTokens> =
    safeAuthCall { api.login(AuthRequest(email, password)) }

  override suspend fun register(email: String, password: String): Result<AuthTokens> =
    safeAuthCall { api.register(RegisterRequest(email, password)) }

  override suspend fun logout(): Result<Unit> =
    safeResponse.safeResponse {
      api.logout().getOrThrow()
      Result.success(Unit)
    }

  override suspend fun google(idToken: String): Result<AuthTokens> =
    safeAuthCall { api.google(GoogleAuthRequest(idToken)) }

  override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> =
    safeResponse.safeResponse {
      api.resetPassword(ResetPasswordRequest(token, newPassword)).getOrThrow()
      Result.success(Unit)
    }

  override suspend fun requestPasswordReset(email: String): Result<Unit> =
    safeResponse.safeResponse {
      api.requestPasswordReset(RequestPasswordResetRequest(email)).getOrThrow()
      Result.success(Unit)
    }

  override suspend fun requestVerify(): Result<Unit> =
    safeResponse.safeResponse {
      api.requestVerify().getOrThrow()
      Result.success(Unit)
    }

  // --- PRIVATE HELPERS ---

  private suspend fun safeAuthCall(
    block: suspend () -> ApiResponse<AuthTokens>
  ): Result<AuthTokens> = safeResponse.safeResponse {
    val result = block().getOrThrow()

    if (result == null)
      Result.failure(VirtualClubException(ErrorType.INTERNAL_ERROR))
    else
      Result.success(result)
  }
}