package es.virtualclubs.domain.repository

import es.virtualclubs.data.models.User

interface UserRepository {
  suspend fun getUserInfo(): Result<User>
}
