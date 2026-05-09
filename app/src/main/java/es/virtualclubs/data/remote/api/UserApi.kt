package es.virtualclubs.data.remote.api

import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.data.remote.dto.UserInfoDto
import retrofit2.http.GET

interface UserApi {
  @GET(Endpoint.getUserInfo)
  suspend fun getUserInfo(): ApiResponse<UserInfoDto>
}
