package es.virtualclubs.data.managers

import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException

object SafeCall {
  suspend fun <T> safeCall(
    block: suspend () -> Result<T>
  ): Result<T> {
    val result = block()

    if (result.isFailure) {
      GlobalUIManager.handleError(
        result.exceptionOrNull() ?: VirtualClubException(ErrorType.INTERNAL_ERROR)
      )
    }

    return result
  }
}