package es.virtualclubs.data.repository

import es.virtualclubs.data.managers.SafeResponse
import es.virtualclubs.data.models.User
import es.virtualclubs.data.remote.api.UserApi
import es.virtualclubs.data.remote.dto.getOrThrow
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.repository.UserRepository

class UserRepositoryImpl(
  private val api: UserApi,
  private val safeResponse: SafeResponse
) : UserRepository {
  override suspend fun getUserInfo(): Result<User> =
    safeResponse.safeResponse {
      val dto = api.getUserInfo().getOrThrow()
        ?: return@safeResponse Result.failure(VirtualClubException(ErrorType.USER_NOT_FOUND))
      Result.success(User(email = dto.email))
    }
}
