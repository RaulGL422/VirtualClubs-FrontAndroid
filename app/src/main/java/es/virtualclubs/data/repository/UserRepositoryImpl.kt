package es.virtualclubs.data.repository

import es.virtualclubs.data.managers.SafeResponse
import es.virtualclubs.data.remote.api.UserApi
import es.virtualclubs.data.remote.dto.getOrThrow
import es.virtualclubs.domain.repository.UserRepository

class UserRepositoryImpl(
  private val api: UserApi,
  private val safeResponse: SafeResponse
) : UserRepository {
  override suspend fun getUserInfo(): Result<Unit> =
    safeResponse.safeResponse {
      // TODO Modificar para controlar los parametros que devuelve y guardarlos en alguna variable
      api.getUserInfo().getOrThrow()
      Result.success(Unit)
    }
}