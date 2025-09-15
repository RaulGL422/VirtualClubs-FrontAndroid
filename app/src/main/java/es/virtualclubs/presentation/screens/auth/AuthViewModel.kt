@file:Suppress("DEPRECATION")

package es.virtualclubs.presentation.screens.auth

import android.R.attr.data
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.credentials.CreateCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.virtualclubs.BuildConfig
import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.domain.repository.RefreshRepository
import es.virtualclubs.domain.usecase.token.GetRefreshTokenUseCase
import es.virtualclubs.domain.usecase.token.SaveTokensUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import androidx.credentials.CredentialManager
import androidx.credentials.CreateCredentialResponse
import androidx.credentials.CredentialManagerCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val refreshRepository: RefreshRepository,
    private val userPreferences: UserPreferences,
    private val saveTokensUseCase: SaveTokensUseCase,
    @param:ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

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
            Log.e("GoogleSignIn", "Sign-in fallido")
            onLoginFailed("google_login_sign-in_failed")
            return
        }

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account: GoogleSignInAccount = task.getResult(Exception::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = repository.google(idToken)
                    if (response.isSuccess) {
                        val tokens = response.getOrNull()
                        if (tokens != null) {
                            _uiState.value = AuthUiState.Success
                            saveTokensUseCase(tokens.accessToken, tokens.refreshToken)
                        } else {
                            _uiState.value = AuthUiState.AuthFailed("missing_tokens")
                        }
                    } else {
                        _uiState.value = AuthUiState.AuthFailed(
                            response.exceptionOrNull()?.message ?: "unknown_error"
                        )
                    }
                }
            } else {
                onLoginFailed("google_login_no_token")
            }
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Error obteniendo cuenta: ${e.message}")
            onLoginFailed("google_login_exception")
        }
    }

    fun onLoginFailed(message: String) {
        _uiState.value = AuthUiState.AuthFailed(message)
    }

    private fun tryAutoLogin() {
        viewModelScope.launch {
            if (userPreferences.autoLoginFlow.firstOrNull() == true) {
                // Try to get a new access token from backend
                val response = refreshRepository.refresh(false)
                val tokens = response.getOrNull()
                if (response.isSuccess && tokens != null) {
                    _uiState.value = AuthUiState.Success
                } else {
                    _uiState.value = AuthUiState.Idle
                }
            }
        }
    }

    fun loginUser(email: String, password: String, rememberUser: Boolean) {
        Log.i("Login", "Attempting login for email: $email")

        viewModelScope.launch {
            _uiState.value = AuthUiState.AttemptingAuth
            val response = repository.login(email, password)
            if (response.isSuccess) {
                val tokens = response.getOrNull()
                if (tokens != null) {
                    saveTokensUseCase(tokens.accessToken, tokens.refreshToken)
                    userPreferences.saveUser(email,rememberUser)
                    _uiState.value = AuthUiState.Success
                } else {
                    _uiState.value = AuthUiState.AuthFailed("missing_tokens")
                }
            } else {
                _uiState.value = AuthUiState.AuthFailed(response.exceptionOrNull()?.message ?: "unknown_error")
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
                _uiState.value = AuthUiState.AuthFailed("passwords_not_equals")
                return@launch
            }

            val response = repository.register(email, password)
            if (response.isSuccess) {
                val tokens = response.getOrNull()
                if (tokens != null) {
                    userPreferences.saveUser(email,rememberUser)
                    saveTokensUseCase(tokens.accessToken, tokens.refreshToken)
                    _uiState.value = AuthUiState.Success
                } else {
                    _uiState.value = AuthUiState.AuthFailed("missing_tokens")
                }
            } else {
                _uiState.value = AuthUiState.AuthFailed(response.exceptionOrNull()?.message ?: "unknown_error")
            }
        }
    }
}

sealed class AuthUiState {
    object Success: AuthUiState()
    data class AuthFailed(val message: String) : AuthUiState()
    object AttemptingAuth : AuthUiState()
    object Idle: AuthUiState()
}