package es.virtualclubs.data.managers

import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException

/**
 * Wrapper para llamadas a casos de uso que propagan errores al [GlobalUIManager].
 *
 * Ejecuta [block] y, si el resultado es un fallo, llama a [GlobalUIManager.handleError]
 * para que la UI lo muestre de forma centralizada. Siempre retorna el [Result] original.
 */
object SafeCall {
  /**
   * Ejecuta [block] de forma segura.
   * @param block Llamada suspendida que retorna [Result].
   * @return El mismo [Result] que devuelve [block]; nunca lanza excepciones.
   */
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