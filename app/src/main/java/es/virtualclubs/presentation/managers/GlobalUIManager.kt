package es.virtualclubs.presentation.managers

import androidx.compose.runtime.staticCompositionLocalOf
import dagger.Lazy
import es.virtualclubs.domain.model.ErrorDispatcher
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.presentation.components.dialogs.EmailNotVerifiedDialog
import es.virtualclubs.presentation.dialogs.VCDialog
import es.virtualclubs.presentation.handlers.ErrorHandler
import es.virtualclubs.presentation.navigation.AppNavigator
import es.virtualclubs.presentation.navigation.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor global de estado de UI: loading, diálogos y errores.
 *
 * Singleton inyectable por Hilt que implementa [ErrorDispatcher], permitiendo que
 * la capa de datos ([SafeCall]) notifique errores sin depender de la presentación.
 * Se vincula en el grafo de Hilt a través de [DispatcherModule].
 *
 * En composables, acceder vía [LocalGlobalUIManager].current.
 *
 * Responsabilidades:
 * - Estado de carga global ([isLoading])
 * - Mostrar/ocultar diálogos tipados ([VCDialog])
 * - Centralizar errores y enrutarlos a la UI o a [AppNavigator]
 */
@Singleton
class GlobalUIManager @Inject constructor(
    private val appNavigator: Lazy<AppNavigator>,
    private val authRepository: Lazy<AuthRepository>,
    private val sessionManager: Lazy<SessionManager>
) : ErrorDispatcher {

    // ─── Loading ─────────────────────────────────────────────────────────────

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun showLoading() { _isLoading.value = true }
    fun hideLoading() { _isLoading.value = false }

    /**
     * Ejecuta [block] mostrando el indicador de carga durante su duración.
     * Garantiza que el loading se oculte aunque [block] lance una excepción.
     */
    suspend fun <T> withLoading(block: suspend () -> T): T {
        showLoading()
        return try { block() } finally { hideLoading() }
    }

    // ─── Dialogs ─────────────────────────────────────────────────────────────

    data class DialogState(
        val visible: Boolean = false,
        val dialog: VCDialog? = null
    )

    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> = _dialogState

    /** Muestra [dialog] de forma modal. Reemplaza cualquier diálogo activo. */
    fun showDialog(dialog: VCDialog) {
        _dialogState.value = DialogState(visible = true, dialog = dialog)
    }

    fun hideDialog() {
        _dialogState.value = DialogState()
    }

    // ─── Errors ──────────────────────────────────────────────────────────────

    private val _errorState = MutableStateFlow(ErrorUiState())
    val errorState: StateFlow<ErrorUiState> = _errorState

    override fun handleError(throwable: Throwable) {
        val code = (throwable as? VirtualClubException)?.errorType ?: ErrorType.INTERNAL_ERROR
        _errorState.value = ErrorUiState(code)

        when (code) {
            ErrorType.EMAIL_NOT_VERIFIED -> showDialog(EmailNotVerifiedDialog { requestVerifyEmail() })
            ErrorType.INVALID_REFRESH_TOKEN,
            ErrorType.MISSING_TOKENS -> CoroutineScope(Dispatchers.IO).launch {
                sessionManager.get().logout()
            }
            else -> Unit
        }
    }

    override fun clearError() { _errorState.value = ErrorUiState() }

    override fun getErrorId(): Int {
        val code = _errorState.value.code ?: return 0
        return ErrorHandler.getErrorMessage(code)
    }

    override fun haveError(): Boolean = _errorState.value.code != null

    /** Establece un error puntual sin pasar por [handleError] (sin efectos secundarios). */
    fun setError(code: ErrorType) { _errorState.value = ErrorUiState(code) }

    /** Solicita al backend el reenvío del email de verificación. */
    fun requestVerifyEmail() {
        CoroutineScope(Dispatchers.IO).launch {
            try { authRepository.get().requestVerify() } catch (_: Exception) { }
        }
    }
}

data class ErrorUiState(val code: ErrorType? = null)

/**
 * CompositionLocal para acceder a [GlobalUIManager] desde composables sin prop-drilling.
 *
 * Provisto en [VirtualClubsMainApp] mediante [CompositionLocalProvider].
 *
 * Uso: `val globalUI = LocalGlobalUIManager.current`
 */
val LocalGlobalUIManager = staticCompositionLocalOf<GlobalUIManager> {
    error("No GlobalUIManager provided. Wrap your composables with CompositionLocalProvider(LocalGlobalUIManager provides ...)")
}
