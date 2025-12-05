package es.virtualclubs.data.managers

import androidx.compose.runtime.Composable
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.presentation.handlers.ErrorHandler
import es.virtualclubs.presentation.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

object ErrorManager {
  private val _errorState = MutableStateFlow(ErrorUiState())
  val errorState: StateFlow<ErrorUiState> = _errorState

  fun handleError(e: Throwable) {
    val vcError = e as? VirtualClubException
    _errorState.value = ErrorUiState(code = vcError?.errorType ?: ErrorType.INTERNAL_ERROR)

    if (errorState.value.code == ErrorType.EMAIL_NOT_VERIFIED) {
      AppNavigator.navigateToVerifyEmail()
    }
  }

  fun clearError() {
    _errorState.value = ErrorUiState()
  }

  fun setError(code: ErrorType) {
    _errorState.value = ErrorUiState(code)
  }

  @Composable
  fun getErrorId() : Int {
    if (!haveError()) return 0
    return ErrorHandler.getErrorMessage(_errorState.value.code!!)
  }

  fun getErrorCode() : Int? {
    return _errorState.value.code?.code
  }

  fun haveError() : Boolean {
    return _errorState.value.code != null
  }
}

data class ErrorUiState(
  val code: ErrorType? = null
)