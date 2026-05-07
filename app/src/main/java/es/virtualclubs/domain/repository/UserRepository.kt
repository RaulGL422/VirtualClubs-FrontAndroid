package es.virtualclubs.domain.repository

import es.virtualclubs.domain.model.User

interface UserRepository {
  suspend fun getUserInfo(): Result<User>
}
