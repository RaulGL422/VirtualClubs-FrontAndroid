package es.virtualclubs.data.remote.api

import es.virtualclubs.data.remote.dto.ApiResponse
import retrofit2.http.POST

interface UserApi {
  // TODO Modificar para recibir informacion del usuario (Clubes, Club personal, configuraciones, etc)
  @POST("/api/user/getUserInfo")
  suspend fun getUserInfo(): ApiResponse<Unit>
}