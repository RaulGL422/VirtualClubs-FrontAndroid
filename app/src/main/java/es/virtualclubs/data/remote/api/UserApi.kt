package es.virtualclubs.data.remote.api

import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.domain.model.Endpoint
import retrofit2.http.GET

interface UserApi {
  // TODO Modificar para recibir informacion del usuario (Clubes, Club personal, configuraciones, etc)
  @GET(Endpoint.getUserInfo)
  suspend fun getUserInfo(): ApiResponse<Unit>
}