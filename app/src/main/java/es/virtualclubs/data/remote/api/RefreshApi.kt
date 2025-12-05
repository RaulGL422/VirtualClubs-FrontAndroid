package es.virtualclubs.data.remote.api

import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.data.remote.dto.RefreshRequest
import es.virtualclubs.domain.model.AuthTokens
import retrofit2.http.POST

interface RefreshApi {
    @POST("/api/auth/refresh")
    suspend fun refresh(refreshRequest: RefreshRequest): ApiResponse<AuthTokens>
}