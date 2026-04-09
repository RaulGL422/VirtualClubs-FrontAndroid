package es.virtualclubs.presentation.screens.verifyemailresult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class VerifyEmailResultViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  private val status: String = savedStateHandle.get<String>("status") ?: ""

  private val _uiState = MutableStateFlow<VerifyEmailResultUiState>(
    if (status == "success") VerifyEmailResultUiState.Success
    else VerifyEmailResultUiState.Error
  )
  val uiState: StateFlow<VerifyEmailResultUiState> = _uiState.asStateFlow()
}

sealed class VerifyEmailResultUiState {
  object Success : VerifyEmailResultUiState()
  object Error : VerifyEmailResultUiState()
}
