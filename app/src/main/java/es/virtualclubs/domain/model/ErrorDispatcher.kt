package es.virtualclubs.domain.model

/**
 * Contrato para despachar errores, estado de carga y mensajes de error
 * desde cualquier capa hacia la UI de forma centralizada.
 *
 * La implementación concreta (GlobalUIManager) vive en la capa de presentación,
 * mientras que la capa de datos ([SafeCall]) usa esta interfaz sin acoplarse a la UI.
 */
interface ErrorDispatcher {
    /** Procesa un [Throwable] y actualiza el estado de error en la UI. */
    fun handleError(throwable: Throwable)
    /** Limpia el estado de error actual. */
    fun clearError()
    /** Retorna el ID de recurso del mensaje de error actual, o 0 si no hay error. */
    fun getErrorId(): Int
    /** Indica si hay un error activo. */
    fun haveError(): Boolean
}
