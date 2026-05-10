package es.virtualclubs.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.domain.model.Club
import es.virtualclubs.domain.model.SessionState
import es.virtualclubs.domain.usecase.GetSessionStateUseCase
import es.virtualclubs.domain.repository.UserRepository
import es.virtualclubs.presentation.managers.GlobalUIManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val userEmail: String? = null,
    val clubs: List<Club> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository,
    private val getSessionState: GetSessionStateUseCase,
    private val safeCall: SafeCall,
    private val globalUIManager: GlobalUIManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            globalUIManager.withLoading {
                val result = safeCall.safeCall { repository.getUserInfo() }
                if (result.isSuccess) {
                    val email = (getSessionState().first() as? SessionState.LoggedIn)?.user?.email
                    _uiState.update { it.copy(userEmail = email) }
                }
            }
        }
    }
}
