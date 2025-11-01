package es.virtualclubs.domain.repository

import es.virtualclubs.domain.model.AuthTokens

interface AuthRepository {
  suspend fun login(email: String, password: String): Result<AuthTokens>
  suspend fun register(email: String, password: String): Result<AuthTokens>
  suspend fun logout(): Result<Unit>
  suspend fun requestPasswordReset(email: String): Result<Unit>
  suspend fun google(idToken: String): Result<AuthTokens>
  suspend fun resetPassword(token: String, newPassword: String): Result<Unit>
  suspend fun requestVerify(): Result<Unit>
}