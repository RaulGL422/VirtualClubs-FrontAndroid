package es.virtualclubs.data.repository

import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.remote.api.RefreshApi
import es.virtualclubs.data.remote.dto.RefreshRequest
import es.virtualclubs.data.remote.dto.getOrThrow
import es.virtualclubs.domain.repository.RefreshRepository
import es.virtualclubs.presentation.navigation.SessionManager
import es.virtualclubs.data.session.UserSession
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

class RefreshRepositoryImpl @Inject constructor(
  private val api: RefreshApi,
  private val sessionManager: SessionManager,
  private val secureUserPreferences: SecureUserPreferences,
  private val userSession: UserSession
) : RefreshRepository {
  override suspend fun refresh(canLogout: Boolean): Result<Unit> {
    val token = secureUserPreferences.refreshToken.firstOrNull()

    return try {
      if (token == null)
        throw Exception()

      val result = api.refresh(RefreshRequest(token)).getOrThrow() ?: throw Exception()

      secureUserPreferences.saveTokens(result.accessToken, result.refreshToken)
      userSession.cacheAccessToken(result.accessToken)
      Result.success(Unit)
    } catch (e: Exception) {
      if (canLogout)
        sessionManager.logout()

      Result.failure(e)
    }
  }
}