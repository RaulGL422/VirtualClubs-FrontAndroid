package es.virtualclubs.presentation.screens.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.presentation.managers.GlobalUIManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val safeCall: SafeCall,
    private val globalUIManager: GlobalUIManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Idle)
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun resetPassword(token: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.value = ResetPasswordUiState.Attempting

            if (newPassword != confirmPassword) {
                globalUIManager.setError(ErrorType.PASSWORD_NOT_EQUALS)
                _uiState.value = ResetPasswordUiState.Idle
                return@launch
            }

            val response = safeCall.safeCall { repository.resetPassword(token, newPassword) }
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
