package es.virtualclubs.presentation.screens.resetPassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.virtualclubs.data.managers.GlobalUIManager
import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
  private val repository: AuthRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Idle)
  val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

  fun resetPassword(token: String, newPassword: String, confirmPassword: String) {
    Log.i("Reset Password", "Resetting password for token: $token")

    viewModelScope.launch {
      _uiState.value = ResetPasswordUiState.Attempting

      if (newPassword != confirmPassword) {
        GlobalUIManager.setError(ErrorType.PASSWORD_NOT_EQUALS)
        _uiState.value = ResetPasswordUiState.Idle
        return@launch
      }

      val response = SafeCall.safeCall { repository.resetPassword(token, newPassword) }
      _uiState.value = if (response.isSuccess) {
        ResetPasswordUiState.Success
      } else {
        ResetPasswordUiState.Idle
      }
    }
  }
}

sealed class ResetPasswordUiState {
  object Success : ResetPasswordUiState()
  object Attempting : ResetPasswordUiState()
  object Idle : ResetPasswordUiState()
}