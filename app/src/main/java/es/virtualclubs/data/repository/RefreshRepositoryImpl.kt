package es.virtualclubs.data.repository

import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.remote.api.AuthApi
import es.virtualclubs.data.remote.dto.RefreshRequest
import es.virtualclubs.data.remote.dto.getOrThrow
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.repository.RefreshRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class RefreshRepositoryImpl @Inject constructor(
  private val api: AuthApi,
  private val secureUserPreferences: SecureUserPreferences,
  private val userSession: UserSession
) : RefreshRepository {

  override suspend fun refresh(): Result<Unit> {
    val token = secureUserPreferences.refreshToken.firstOrNull()
      ?: return Result.failure(VirtualClubException(ErrorType.MISSING_TOKENS))

    return try {
      val result = api.refresh(RefreshRequest(token)).getOrThrow()
        ?: return Result.failure(VirtualClubException(ErrorType.MISSING_TOKENS))

      secureUserPreferences.saveTokens(result.accessToken, result.refreshToken)
      userSession.cacheAccessToken(result.accessToken)
      Result.success(Unit)
    } catch (_: VirtualClubException) {
      Result.failure(VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN))
    } catch (_: Exception) {
      Result.failure(VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN))
    }
  }
}
