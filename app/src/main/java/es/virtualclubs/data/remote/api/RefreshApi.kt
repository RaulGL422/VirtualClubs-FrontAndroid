package es.virtualclubs.data.remote.api

import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.data.remote.dto.RefreshRequest
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.model.Endpoint
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi {
  @POST(Endpoint.refresh)
  suspend fun refresh(@Body refreshRequest: RefreshRequest): ApiResponse<AuthTokens>
}