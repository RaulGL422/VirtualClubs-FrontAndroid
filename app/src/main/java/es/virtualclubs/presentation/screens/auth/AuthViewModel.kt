@file:Suppress("DEPRECATION")

package es.virtualclubs.presentation.screens.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.virtualclubs.BuildConfig
import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.managers.GlobalUIManager
import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.domain.repository.RefreshRepository
import es.virtualclubs.session.UserSession
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val repository: AuthRepository,
  private val refreshRepository: RefreshRepository,
  private val userPreferences: UserPreferences,
  private val securePreferences: SecureUserPreferences,
  private val userSession: UserSession,
  @param:ApplicationContext private val context: Context
) : ViewModel() {
  private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
  val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
  private val _passwordResetUiState = MutableStateFlow<PasswordResetUiState>(PasswordResetUiState.Idle)
  val passwordResetUiState: StateFlow<PasswordResetUiState> = _passwordResetUiState.asStateFlow()

  init {
    tryAutoLogin()
  }

  // Google
  val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(
    context,
    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID) // Web Client ID
      .requestEmail()
      .build()
  )

  // --- Función para iniciar login ---
  fun beginSignInGoogle(
    googleSignInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
  ) {
    val signInIntent: Intent = googleSignInClient.signInIntent
    googleSignInLauncher.launch(signInIntent)
  }

  // --- Manejo del resultado ---
  fun handleSignInResultGoogle(result: ActivityResult) {
    if (result.resultCode != Activity.RESULT_OK) {
      Log.e(
        "GoogleSignIn",
        "Sign-in con google ha fallado con codigo de error: ${result.resultCode}"
      )
      onLoginFailed(ErrorType.GOOGLE_SIGN_IN_FAILED)
      return
    }

    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
    try {
      val account: GoogleSignInAccount = task.getResult(Exception::class.java)
      val idToken = account.idToken
      if (idToken != null) {
        CoroutineScope(Dispatchers.IO).launch {
          val response = SafeCall.safeCall { repository.google(idToken) }
          if (response.isSuccess) {
            val tokens = response.getOrNull()
            if (tokens != null) {
              userSession.currentUser.copy(email = account.email)
              _uiState.value = AuthUiState.Success
              securePreferences.saveTokens(tokens.accessToken, tokens.refreshToken)
            } else {
              GlobalUIManager.setError(ErrorType.MISSING_TOKENS)
              _uiState.value = AuthUiState.Idle
            }
          } else {
            _uiState.value = AuthUiState.Idle
          }
        }
      } else {
        onLoginFailed(ErrorType.GOOGLE_SIGN_IN_NO_TOKEN)
      }
    } catch (e: Exception) {
      Log.e("GoogleSignIn", "Error obteniendo cuenta: ${e.message}")
      onLoginFailed(ErrorType.GOOGLE_LOGIN_EXCEPTION)
    }
  }

  fun onLoginFailed(errorType: ErrorType) {
    GlobalUIManager.setError(errorType)
    _uiState.value = AuthUiState.Idle
  }

  private fun tryAutoLogin() {
    viewModelScope.launch {
      if (userPreferences.autoLoginFlow.firstOrNull() == true) {
        GlobalUIManager.showLoading()
        // Try to get a new access token from backend
        val response = SafeCall.safeCall { refreshRepository.refresh(false) }
        val tokens = response.getOrNull()
        _uiState.value = if (response.isSuccess && tokens != null) {
          userSession.currentUser.copy(email = userPreferences.userEmailFlow.firstOrNull())
          AuthUiState.Success
        } else {
          AuthUiState.Idle
        }
        GlobalUIManager.hideLoading()
      }
    }
  }

  fun loginUser(email: String, password: String, rememberUser: Boolean) {
    Log.i("Login", "Attempting login for email: $email")

    viewModelScope.launch {
      _uiState.value = AuthUiState.AttemptingAuth
      val response = SafeCall.safeCall { repository.login(email, password) }
      if (response.isSuccess) {
        val tokens = response.getOrNull()
        if (tokens != null) {
          securePreferences.saveTokens(tokens.accessToken, tokens.refreshToken)
          userPreferences.saveUser(email, rememberUser)
          userSession.currentUser.copy(email = email)
          _uiState.value = AuthUiState.Success
        } else {
          GlobalUIManager.setError(ErrorType.MISSING_TOKENS)
          _uiState.value = AuthUiState.Idle
        }
      } else {
        _uiState.value = AuthUiState.Idle
      }
    }
  }

  fun registerUser(
    email: String, password: String, confirmPassword: String, rememberUser: Boolean
  ) {
    Log.i("Register", "Attempting register for email: $email")

    viewModelScope.launch {
      _uiState.value = AuthUiState.AttemptingAuth

      if (password != confirmPassword) {
        GlobalUIManager.setError(ErrorType.PASSWORD_NOT_EQUALS)
        _uiState.value = AuthUiState.Idle
        return@launch
      }

      val response = SafeCall.safeCall { repository.register(email, password) }
      if (response.isSuccess) {
        val tokens = response.getOrNull()
        if (tokens != null) {
          userPreferences.saveUser(email, rememberUser)
          securePreferences.saveTokens(tokens.accessToken, tokens.refreshToken)
          userSession.currentUser.copy(email = email)
          _uiState.value = AuthUiState.Success
        } else {
          GlobalUIManager.setError(ErrorType.MISSING_TOKENS)
          _uiState.value = AuthUiState.Idle
        }
      } else {
        _uiState.value = AuthUiState.Idle
      }
    }
  }

  fun requestPasswordReset(email: String) {
    Log.i("Password Request", "Requesting password reset for email: $email")

    viewModelScope.launch {
      _passwordResetUiState.value = PasswordResetUiState.Attempting

      val response = SafeCall.safeCall { repository.requestPasswordReset(email) }
      if (response.isSuccess) {
        _passwordResetUiState.value = PasswordResetUiState.Success
      } else {
        _passwordResetUiState.value = PasswordResetUiState.Idle
      }
    }
  }

  fun resetPasswordRequest() {
    _passwordResetUiState.value = PasswordResetUiState.Idle
  }
}

sealed class AuthUiState {
  object Success : AuthUiState()
  object AttemptingAuth : AuthUiState()
  object Idle : AuthUiState()
}

sealed class PasswordResetUiState {
  object Success : PasswordResetUiState()
  object Attempting : PasswordResetUiState()
  object Idle : PasswordResetUiState()
}