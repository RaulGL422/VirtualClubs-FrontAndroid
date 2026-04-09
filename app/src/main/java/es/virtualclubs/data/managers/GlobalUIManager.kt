package es.virtualclubs.data.managers

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import dagger.hilt.android.EntryPointAccessors
import es.virtualclubs.App
import es.virtualclubs.di.GlobalUIEntryPoint
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.presentation.components.dialogs.showEmailNotVerifiedDialog
import es.virtualclubs.presentation.handlers.ErrorHandler
import es.virtualclubs.presentation.navigation.AppNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object GlobalUIManager {

  fun getEntryPoint(): GlobalUIEntryPoint {
    val context = App.appContext
    val entryPoint = EntryPointAccessors.fromApplication(
      context,
      GlobalUIEntryPoint::class.java
    )
    return entryPoint
  }

  // ----- Loading -----
  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading

  fun showLoading() { _isLoading.value = true }
  fun hideLoading() { _isLoading.value = false }

  suspend fun <T> withLoading(block: suspend () -> T): T {
    showLoading()
    try {
      return block()
    } finally {
      hideLoading()
    }
  }


  // ----- Dialog -----
  data class DialogState(
    val visible: Boolean = false,
    val title: Int? = null,
    val content: (@Composable ColumnScope.() -> Unit)? = null,
    val dismissible: Boolean = true,
    val blockDialog: Boolean = false,
    val confirmText: Int? = null,
    val onConfirm: (() -> Unit)? = null
  )

  private val _dialogState = MutableStateFlow(DialogState())
  val dialogState: StateFlow<DialogState> = _dialogState

  fun showDialog(
    title: Int?,
    content: (@Composable ColumnScope.() -> Unit)? = null,
    blockDialog: Boolean = false,
    dismissible: Boolean = true,
    onConfirm: (() -> Unit)? = null,
    confirmText: Int? = null
  ) {
    _dialogState.value = DialogState(
      visible = true,
      title = title,
      content = content,
      dismissible = dismissible,
      onConfirm = onConfirm,
      blockDialog = blockDialog,
      confirmText = confirmText
    )
  }

  fun hideDialog() {
    _dialogState.value = DialogState()
  }


  // ----- Errors -----
  private val _errorState = MutableStateFlow(ErrorUiState())
  val errorState: StateFlow<ErrorUiState> = _errorState

  fun handleError(e: Throwable) {
    val vcError = e as? VirtualClubException
    val code = vcError?.errorType ?: ErrorType.INTERNAL_ERROR

    _errorState.value = ErrorUiState(code)

    if (code == ErrorType.EMAIL_NOT_VERIFIED) {
      showEmailNotVerifiedDialog()
    }

    if (code == ErrorType.MISSING_TOKENS) {
      AppNavigator.navigateToLoginAndClearStack()
    }
  }

  fun requestVerifyEmail() {
    CoroutineScope(Dispatchers.IO).launch {
      try {
        getEntryPoint().authRepository().requestVerify()
      } catch (_: Exception) { }
    }
  }

  fun clearError() {
    _errorState.value = ErrorUiState()
  }

  fun setError(code: ErrorType) {
    _errorState.value = ErrorUiState(code)
  }

  fun getErrorId() : Int {
    if (!haveError()) return 0
    return ErrorHandler.getErrorMessage(_errorState.value.code!!)
  }

  fun haveError() : Boolean {
    return _errorState.value.code != null
  }
}

data class ErrorUiState(val code: ErrorType? = null)
