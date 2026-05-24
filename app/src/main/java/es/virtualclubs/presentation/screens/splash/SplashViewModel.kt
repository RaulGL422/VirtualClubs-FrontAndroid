package es.virtualclubs.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.User
import es.virtualclubs.domain.usecase.RefreshTokenUseCase
import es.virtualclubs.domain.usecase.token.GetRefreshTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashDestination {
    object Home : SplashDestination()
    object Login : SplashDestination()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getRefreshToken: GetRefreshTokenUseCase,
    private val refreshToken: RefreshTokenUseCase,
    private val userPreferences: UserPreferences,
    private val userSession: UserSession
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            if (getRefreshToken() == null) {
                _destination.value = SplashDestination.Login
                return@launch
            }
            val result = refreshToken()
            _destination.value = if (result.isSuccess) {
                userSession.login(User(email = userPreferences.userEmailFlow.firstOrNull()))
                SplashDestination.Home
            } else {
                SplashDestination.Login
            }
        }
    }
}
