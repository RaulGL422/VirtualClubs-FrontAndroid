package es.virtualclubs.data.managers

import com.google.gson.Gson
import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.repository.RefreshRepository
import jakarta.inject.Inject
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class SafeResponse @Inject constructor(
  private val refreshRepository: RefreshRepository
) {
  suspend fun <T> safeResponse(
    block: suspend () -> Result<T>
  ): Result<T> {
    return try {
      block()
    } catch (e: CancellationException) {
      throw e
    } catch (_: IOException) {
      Result.failure(VirtualClubException(ErrorType.CANT_CONNECT_SERVER))
    } catch (e: HttpException) {
      if (e.code() == 401) {
        try {
          refreshRepository.refresh().getOrThrow()
          block()
        } catch (_: VirtualClubException) {}
      }

      val errorBody = e.response()?.errorBody()?.string()
      val serverMessage = try {
        Gson().fromJson(errorBody, ApiResponse::class.java)?.message
      } catch (_: Exception) {
        null
      }
      Result.failure(VirtualClubException(ErrorType.fromCode(serverMessage ?: -1)))
    } catch (_: Exception) {
      Result.failure(VirtualClubException(ErrorType.INTERNAL_ERROR))
    }
  }
}