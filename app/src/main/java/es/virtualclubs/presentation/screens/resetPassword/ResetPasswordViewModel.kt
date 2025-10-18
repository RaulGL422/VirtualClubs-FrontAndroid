package es.virtualclubs.presentation.screens.resetPassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.presentation.screens.auth.AuthUiState
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Idle)
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun resetPassword(token: String, newPassword: String, confirmPassword: String) {
        Log.i("Reset Password", "Resetting password for token: $token")

        viewModelScope.launch {
            _uiState.value = ResetPasswordUiState.Attempting

            if (newPassword != confirmPassword) {
                _uiState.value = ResetPasswordUiState.Failed("passwords_not_equals")
                return@launch
            }

            val response = repository.resetPassword(token, newPassword)
            _uiState.value = if (response.isSuccess) {
                ResetPasswordUiState.Success
            } else {
                ResetPasswordUiState.Failed(response.exceptionOrNull()?.message ?: "unknown_error")
            }
        }
    }
}

sealed class ResetPasswordUiState {
    object Success : ResetPasswordUiState()
    data class Failed(val message: String) : ResetPasswordUiState()
    object Attempting : ResetPasswordUiState()
    object Idle : ResetPasswordUiState()
}