package es.virtualclubs.data.managers

import com.google.gson.Gson
import es.virtualclubs.data.remote.dto.ApiResponse
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.repository.RefreshRepository
import javax.inject.Inject
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Wrapper de red que centraliza el manejo de errores HTTP y el refresco automático de tokens.
 *
 * - **401**: intenta refrescar el access token vía [RefreshRepository] y reintenta [block].
 *   Si el refresh también falla, retorna [ErrorType.INVALID_REFRESH_TOKEN].
 * - **5xx**: retorna [ErrorType.INTERNAL_ERROR].
 * - **4xx**: parsea el body de error con Gson; si falla, usa [fallbackErrorTypeFromHttpCode].
 * - **IOException**: retorna [ErrorType.CANT_CONNECT_SERVER].
 * - **CancellationException**: se propaga sin capturar (no interrumpir coroutines).
 */
class SafeResponse @Inject constructor(
  private val refreshRepository: RefreshRepository,
  private val gson: Gson
) {
  /**
   * Ejecuta [block] con manejo centralizado de errores de red.
   * @param block Llamada suspendida que retorna [Result].
   */
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
      when {
        e.code() == 401 -> {
          try {
            refreshRepository.refresh(true).getOrThrow()
            block()
          } catch (ex: VirtualClubException) {
            Result.failure(ex)
          } catch (_: Exception) {
            Result.failure(VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN))
          }
        }
        e.code() >= 500 -> Result.failure(VirtualClubException(ErrorType.INTERNAL_ERROR))
        else -> {
          val errorType = parseErrorBody(e.response()?.errorBody()?.string())
            ?: fallbackErrorTypeFromHttpCode(e.code())
          Result.failure(VirtualClubException(errorType))
        }
      }
    } catch (_: Exception) {
      Result.failure(VirtualClubException(ErrorType.INTERNAL_ERROR))
    }
  }

  private fun parseErrorBody(errorBody: String?): ErrorType? {
    if (errorBody.isNullOrBlank()) return null
    return try {
      val code = gson.fromJson(errorBody, ApiResponse::class.java)
        ?.message
        ?.takeIf { it > 0 }
        ?: return null
      ErrorType.fromCode(code)
    } catch (_: Exception) {
      null
    }
  }

  private fun fallbackErrorTypeFromHttpCode(code: Int): ErrorType = when (code) {
    400 -> ErrorType.FIELD_BLANK
    403 -> ErrorType.EMAIL_NOT_VERIFIED
    404 -> ErrorType.USER_NOT_FOUND
    409 -> ErrorType.EMAIL_ALREADY_EXISTS
    else -> ErrorType.INTERNAL_ERROR
  }
}
