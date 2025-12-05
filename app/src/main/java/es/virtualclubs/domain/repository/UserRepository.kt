package es.virtualclubs.domain.repository

interface UserRepository {
  suspend fun getUserInfo(): Result<Unit>
}