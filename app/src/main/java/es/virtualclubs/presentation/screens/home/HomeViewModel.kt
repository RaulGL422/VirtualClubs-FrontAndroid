package es.virtualclubs.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.virtualclubs.data.managers.GlobalUIManager
import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.domain.repository.UserRepository
import es.virtualclubs.session.UserSession
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repository: UserRepository,
  private val userSession: UserSession
) : ViewModel() {
  init {
    viewModelScope.launch {
      GlobalUIManager.showLoading()
      // Obtain user information
      val result = SafeCall.safeCall { repository.getUserInfo() }
      if (result.isSuccess) {
        // TODO
      }
      GlobalUIManager.hideLoading()
    }
  }
}