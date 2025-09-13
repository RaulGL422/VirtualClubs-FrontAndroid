package es.virtualclubs.data.remote.api

import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.data.remote.dto.AuthRequest
import es.virtualclubs.data.remote.dto.GoogleAuthRequest
import es.virtualclubs.data.remote.dto.LogoutRequest
import es.virtualclubs.data.remote.dto.RefreshRequest
import es.virtualclubs.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/authenticate")
    suspend fun login(@Body request: AuthRequest): ApiResponse<Map<String, String>>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<Map<String, String>>

    @POST("/api/auth/logout")
    suspend fun logout(@Body request: LogoutRequest): ApiResponse<Unit>

    @POST("/api/auth/google")
    suspend fun google(@Body request: GoogleAuthRequest): ApiResponse<Map<String, String>>
}