package es.virtualclubs.data.repository

import com.google.gson.Gson
import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.domain.repository.RefreshRepository
import es.virtualclubs.presentation.navigation.SessionManager
import es.virtualclubs.domain.usecase.RefreshTokenUseCase
import es.virtualclubs.domain.usecase.token.GetRefreshTokenUseCase
import es.virtualclubs.domain.usecase.token.SaveTokensUseCase
import jakarta.inject.Inject
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class SafeCall @Inject constructor(
    private val sessionManager: SessionManager,
    private val refreshRepository: RefreshRepository,
    private val refreshToken: GetRefreshTokenUseCase,
    private val saveTokens: SaveTokensUseCase,
) {
    suspend fun <T> safeCall(
        block: suspend () -> Result<T>
    ): Result<T> {
        return try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (_: IOException) {
            Result.failure(Exception("cant_connect_server"))
        } catch (e: HttpException) {
            if (e.code() == 401) {
                val refreshed = tryRefreshToken()
                if (refreshed) {
                    return block()
                } else {
                    sessionManager.logout(refreshToken.invoke() ?: "")
                    return Result.failure(Exception("unauthorized"))
                }
            }

            val errorBody = e.response()?.errorBody()?.string()
            val serverMessage = try {
                Gson().fromJson(errorBody, ApiResponse::class.java)?.message
            } catch (_: Exception) {
                null
            }
            Result.failure(Exception(serverMessage ?: "server_error"))
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "unknown_error"))
        }
    }

    private suspend fun tryRefreshToken(): Boolean {
        val refreshToken = refreshToken() ?: return false
        val result = refreshRepository.refresh(refreshToken)
        return if (result.isSuccess) {
            val tokens = result.getOrNull()!!
            saveTokens(tokens.accessToken, tokens.refreshToken)
            true
        } else {
            false
        }
    }
}