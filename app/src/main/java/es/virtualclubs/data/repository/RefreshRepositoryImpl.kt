package es.virtualclubs.data.repository

import es.virtualclubs.data.remote.api.RefreshApi
import es.virtualclubs.data.remote.dto.RefreshRequest
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
            if (token == null) {
                throw Exception("no_refresh_token")
            }

            val response = api.refresh(RefreshRequest(token))
            if (response.success && response.data != null) {
                val tokens = AuthTokens(
                    accessToken = response.data["accessToken"] ?: "",
                    refreshToken = response.data["refreshToken"] ?: ""
                )

                saveTokens(tokens.accessToken, tokens.refreshToken)
                Result.success(Unit)
            } else {
                throw Exception(response.message)
            }
        } catch (e: Exception) {
            if (canLogout) {
                sessionManager.logout(token ?: "")
            }
            Result.failure(e)
        }
    }
}