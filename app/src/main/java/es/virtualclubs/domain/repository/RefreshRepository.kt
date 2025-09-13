package es.virtualclubs.domain.repository

import es.virtualclubs.domain.model.AuthTokens

interface RefreshRepository {
    suspend fun refresh(token: String): Result<AuthTokens>
}