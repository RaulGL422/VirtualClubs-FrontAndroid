package es.virtualclubs.domain.repository

interface RefreshRepository {
    suspend fun refresh(): Result<Unit>
}