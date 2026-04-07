package es.virtualclubs.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.virtualclubs.data.managers.GlobalUIManager
import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.data.models.Club
import es.virtualclubs.domain.repository.UserRepository
import es.virtualclubs.session.UserSession
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val userEmail: String? = null,
    val clubs: List<Club> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            GlobalUIManager.withLoading {
                val result = SafeCall.safeCall { repository.getUserInfo() }
                if (result.isSuccess) {
                    _uiState.update { it.copy(userEmail = userSession.currentUser.email) }
                }
            }
        }
    }
}
