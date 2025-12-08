package es.virtualclubs.data.managers

object SafeCall {
  suspend fun <T> safeCall(
    block: suspend () -> Result<T>
  ): Result<T> {
    val result = block()

    if (result.isFailure) {
      GlobalUIManager.handleError(result.exceptionOrNull()!!)
    }

    return result
  }
}