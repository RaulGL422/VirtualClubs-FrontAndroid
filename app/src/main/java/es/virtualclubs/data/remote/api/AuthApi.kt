package es.virtualclubs.data.remote.api

import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.data.remote.dto.AuthRequest
import es.virtualclubs.data.remote.dto.GoogleAuthRequest
import es.virtualclubs.data.remote.dto.RegisterRequest
import es.virtualclubs.data.remote.dto.RequestPasswordResetRequest
import es.virtualclubs.data.remote.dto.ResetPasswordRequest
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.model.Endpoint
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApi {
    @POST(Endpoint.login)
    suspend fun login(@Body request: AuthRequest): ApiResponse<AuthTokens>

    @POST(Endpoint.register)
    suspend fun register(@Body request: RegisterRequest): ApiResponse<AuthTokens>

    @DELETE(Endpoint.logout)
    suspend fun logout(): ApiResponse<Unit>

    @POST(Endpoint.google)
    suspend fun google(@Body request: GoogleAuthRequest): ApiResponse<AuthTokens>

    @POST(Endpoint.requestPasswordReset)
    suspend fun requestPasswordReset(@Body request: RequestPasswordResetRequest): ApiResponse<Unit>

    @POST(Endpoint.resetPassword)
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ApiResponse<Unit>

    @POST(Endpoint.requestVerify)
    suspend fun requestVerify(): ApiResponse<Unit>
}