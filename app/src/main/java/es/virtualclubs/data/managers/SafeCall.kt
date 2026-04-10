package es.virtualclubs.data.managers

import es.virtualclubs.domain.model.ErrorDispatcher
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper para llamadas a casos de uso que propagan errores al [ErrorDispatcher].
 *
 * Ejecuta [block] y, si el resultado es un fallo, notifica al [ErrorDispatcher]
 * para que la UI lo muestre de forma centralizada. Siempre retorna el [Result] original.
 *
 * Se inyecta vía Hilt — la implementación concreta de [ErrorDispatcher] es
 * [GlobalUIManager], vinculada en [DispatcherModule].
 */
@Singleton
class SafeCall @Inject constructor(
    private val errorDispatcher: ErrorDispatcher
) {
    /**
     * Ejecuta [block] de forma segura y notifica errores al [ErrorDispatcher].
     * @return El mismo [Result] que devuelve [block]; nunca lanza excepciones.
     */
    suspend fun <T> safeCall(block: suspend () -> Result<T>): Result<T> {
        val result = block()
        if (result.isFailure) {
            errorDispatcher.handleError(
                result.exceptionOrNull() ?: VirtualClubException(ErrorType.INTERNAL_ERROR)
            )
        }
        return result
    }
}
