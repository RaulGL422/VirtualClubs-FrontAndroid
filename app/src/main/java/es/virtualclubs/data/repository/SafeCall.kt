package es.virtualclubs.data.repository

import com.google.gson.Gson
import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.domain.repository.RefreshRepository
import jakarta.inject.Inject
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class SafeCall @Inject constructor(
    private val refreshRepository: RefreshRepository
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
                val result = refreshRepository.refresh()
                if (result.isSuccess) {
                    block()
                } else {
                    Result.failure(Exception(result.exceptionOrNull()?.message ?: "unauthorized"))
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
}