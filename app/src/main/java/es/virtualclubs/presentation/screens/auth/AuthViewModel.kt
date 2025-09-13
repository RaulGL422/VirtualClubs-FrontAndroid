package es.virtualclubs.presentation.screens.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
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

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val refreshRepository: RefreshRepository,
    private val userPreferences: UserPreferences,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase,
    private val saveTokensUseCase: SaveTokensUseCase,
    @param:ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        tryAutoLogin()
    }

    // Google
    val oneTapClientGoogle = Identity.getSignInClient(context)

    var googleSignIn = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    fun beginSignInGoogle(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) {
        _uiState.value = AuthUiState.AttemptingAuth

        oneTapClientGoogle.beginSignIn(googleSignIn)
            .addOnSuccessListener { result ->
                try {
                    launcher.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )
                } catch (e: Exception) {
                    onLoginFailed("google_login_launch_failed")
                }
            }
            .addOnFailureListener { e ->
                onLoginFailed("google_login_sign-in_failed")
            }
    }

    fun handleSignInResultGoogle(activityResult: ActivityResult) {
        if (activityResult.resultCode != Activity.RESULT_OK) {
            onLoginFailed("google_login_sign-in_failed")
            return
        }

        val data = activityResult.data

        try {
            val credential = oneTapClientGoogle.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken

            if (idToken != null) {
                viewModelScope.launch {
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
                        _uiState.value = AuthUiState.AuthFailed(response.exceptionOrNull()?.message ?: "unknown_error")
                    }
                }
            } else {
                onLoginFailed("google_login_no_token")
            }
        } catch (e: ApiException) {
            onLoginFailed("google_login_api_exception")
        }
    }

    fun onLoginFailed(message: String) {
        _uiState.value = AuthUiState.AuthFailed(message)
    }

    private fun tryAutoLogin() {
        viewModelScope.launch {
            if (userPreferences.autoLoginFlow.firstOrNull() == true) {
                val refreshToken = getRefreshTokenUseCase()
                if (refreshToken != null) {
                    // Try to get a new access token from backend
                    val response = refreshRepository.refresh(refreshToken)
                    if (response.isSuccess) {
                        val tokens = response.getOrNull()
                        if (tokens != null) {
                            saveTokensUseCase(tokens.accessToken, tokens.refreshToken)
                            _uiState.value = AuthUiState.Success
                        } else {
                            _uiState.value = AuthUiState.Idle
                        }
                    } else {
                        _uiState.value = AuthUiState.Idle
                    }
                } else {
                    // No saved refresh token → show login screen
                    _uiState.value = AuthUiState.Idle
                }
            }
        }
    }

    fun loginUser(email: String, password: String) {
        Log.i("Login", "Attempting login for email: $email")

        viewModelScope.launch {
            _uiState.value = AuthUiState.AttemptingAuth
            val response = repository.login(email, password)
            if (response.isSuccess) {
                val tokens = response.getOrNull()
                if (tokens != null) {
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

    fun registerUser(
        email: String, password: String, confirmPassword: String
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