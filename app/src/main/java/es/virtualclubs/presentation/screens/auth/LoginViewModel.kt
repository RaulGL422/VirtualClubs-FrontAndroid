package es.iesfernandoaguilar.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.iesfernandoaguilar.R
import es.iesfernandoaguilar.models.ViewModelControl
import es.virtualclubs.models.objects.ErrorHandler
import es.iesfernandoaguilar.models.objects.SessionData
import es.iesfernandoaguilar.models.objects.ViewModelActive
import es.iesfernandoaguilar.ui.manager.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModelControl, ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    init {
        ViewModelActive.setActive(this) // Set active ViewModel
        sessionManager.autoLogin() // Attempt auto-login
    }

    fun loginUser() {
        if (!validateLoginInput(_state.value.email, _state.value.password)) return

        Log.i("Login", "Attempting login for email: ${_state.value.email}")

        viewModelScope.launch {
            applySessionState(_state.value.password, _state.value.checkedAutologinBox) // Save session info
            sessionManager.loginUser(_state.value.email)
        }
    }

    fun registerUser(
    ) {
        if (!validateRegisterInput(_state.value.username, _state.value.email, _state.value.password, _state.value.confirmPassword)) return

        Log.i("Register", "Attempting register for user: ${_state.value.username} | email: ${_state.value.email}")

        viewModelScope.launch {
            applySessionState(_state.value.password, _state.value.checkedAutologinBox) // Save session info
            sessionManager.registerUser(_state.value.username, _state.value.email)
        }
    }

    fun updateUsername(username: String) {
        _state.value = _state.value.copy(username = username)
    }

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _state.value = _state.value.copy(confirmPassword = confirmPassword)
    }

    fun updateCheckedAutologinBox(checked: Boolean) {
        _state.value = _state.value.copy(checkedAutologinBox = checked)
    }

    private fun validateLoginInput(email: String, password: String): Boolean {
        return when {
            email.isBlank() -> showError(R.string.email_cant_be_empty)
            password.isBlank() -> showError(R.string.password_cant_be_empty)
            else -> true
        }
    }

    private fun validateRegisterInput(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return when {
            username.isBlank() -> showError(R.string.username_cant_be_empty)
            email.isBlank() -> showError(R.string.email_cant_be_empty)
            password != confirmPassword -> showError(R.string.password_dont_equals)
            !PasswordUtils.verifyPassword(password) -> false // PasswordUtils shows errors
            else -> true
        }
    }

    private fun showError(messageRes: Int): Boolean {
        ErrorHandler.showError(messageRes)
        return false
    }

    private fun applySessionState(password: String, autoLogin: Boolean) {
        SessionData.passWord = password
        SessionData.autoLoginActive = autoLogin
    }
}

data class LoginState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val checkedAutologinBox: Boolean = false
)