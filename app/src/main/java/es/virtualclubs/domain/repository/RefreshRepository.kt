package es.virtualclubs.domain.repository

interface RefreshRepository {
    suspend fun refresh(canLogout: Boolean = true): Result<Unit>
}