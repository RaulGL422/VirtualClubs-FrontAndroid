package es.virtualclubs.domain.repository

import es.virtualclubs.domain.model.AuthTokens

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthTokens>
    suspend fun register(email: String, password: String): Result<AuthTokens>
    suspend fun logout(refreshToken: String): Result<Unit>
    suspend fun google(idToken: String): Result<AuthTokens>
}