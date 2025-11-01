package es.virtualclubs.data.repository

import es.virtualclubs.data.remote.api.RefreshApi
import es.virtualclubs.data.remote.dto.RefreshRequest
import es.virtualclubs.data.remote.dto.getOrThrow
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.repository.RefreshRepository
import es.virtualclubs.domain.usecase.token.GetRefreshTokenUseCase
import es.virtualclubs.domain.usecase.token.SaveTokensUseCase
import es.virtualclubs.presentation.navigation.SessionManager
import jakarta.inject.Inject

class RefreshRepositoryImpl @Inject constructor(
  private val api: RefreshApi,
  private val sessionManager: SessionManager,
  private val saveTokens: SaveTokensUseCase,
  private val refreshToken: GetRefreshTokenUseCase
) : RefreshRepository {
  override suspend fun refresh(canLogout: Boolean): Result<Unit> {
    val token = refreshToken.invoke()

    return try {
      if (token == null)
        throw Exception()

      val result = api.refresh(RefreshRequest(token)).getOrThrow()
      saveTokens(result.accessToken, result.refreshToken)
      Result.success(Unit)
    } catch (e: Exception) {
      if (canLogout)
        sessionManager.logout(token ?: "")

      Result.failure(e)
    }
  }
}