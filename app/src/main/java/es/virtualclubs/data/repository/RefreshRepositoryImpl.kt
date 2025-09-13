package es.virtualclubs.data.repository

import es.virtualclubs.data.remote.api.RefreshApi
import es.virtualclubs.data.remote.dto.RefreshRequest
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.repository.RefreshRepository
import jakarta.inject.Inject

class RefreshRepositoryImpl @Inject constructor(
    private val api: RefreshApi,
) : RefreshRepository {
    override suspend fun refresh(token: String): Result<AuthTokens> {
        val response = api.refresh(RefreshRequest(token))
        if (response.success && response.data != null) {
            val tokens = AuthTokens(
                accessToken = response.data["accessToken"] ?: "",
                refreshToken = response.data["refreshToken"] ?: ""
            )
            return Result.success(tokens)
        } else {
            return Result.failure(Exception(response.message))
        }
    }
}