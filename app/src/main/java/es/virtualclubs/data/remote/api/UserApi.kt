package es.virtualclubs.data.remote.api

import es.virtualclubs.data.remote.dto.ApiResponse
import retrofit2.http.GET

interface UserApi {
  // TODO Modificar para recibir informacion del usuario (Clubes, Club personal, configuraciones, etc)
  @GET("/api/user/getUserInfo")
  suspend fun getUserInfo(): ApiResponse<Unit>
}