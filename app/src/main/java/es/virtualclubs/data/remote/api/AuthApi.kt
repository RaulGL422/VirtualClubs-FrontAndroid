package es.virtualclubs.data.remote.api

import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.data.remote.dto.AuthRequest
import es.virtualclubs.data.remote.dto.GoogleAuthRequest
import es.virtualclubs.data.remote.dto.RegisterRequest
import es.virtualclubs.data.remote.dto.RequestPasswordResetRequest
import es.virtualclubs.data.remote.dto.ResetPasswordRequest
import es.virtualclubs.domain.model.AuthTokens
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/authenticate")
    suspend fun login(@Body request: AuthRequest): ApiResponse<AuthTokens>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<AuthTokens>

    @POST("/api/auth/logout")
    suspend fun logout(): ApiResponse<Unit>

    @POST("/api/auth/google")
    suspend fun google(@Body request: GoogleAuthRequest): ApiResponse<AuthTokens>

    @POST("/api/auth/requestPasswordRequest")
    suspend fun requestPasswordReset(@Body request: RequestPasswordResetRequest): ApiResponse<Unit>

    @POST("/api/auth/resetPassword")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ApiResponse<Unit>

    @POST("/api/auth/requestVerify")
    suspend fun requestVerify(): ApiResponse<Unit>
}