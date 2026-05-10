package es.virtualclubs.presentation.screens.auth

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.virtualclubs.BuildConfig
import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.User
import es.virtualclubs.domain.usecase.AuthUseCase
import es.virtualclubs.domain.usecase.GoogleUseCase
import es.virtualclubs.domain.usecase.RefreshTokenUseCase
import es.virtualclubs.domain.usecase.RegisterUseCase
import es.virtualclubs.domain.usecase.RequestPasswordResetUseCase
import es.virtualclubs.domain.usecase.token.GetRefreshTokenUseCase
import es.virtualclubs.domain.usecase.token.SaveTokensUseCase
import es.virtualclubs.presentation.managers.GlobalUIManager
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val registerUseCase: RegisterUseCase,
    private val googleUseCase: GoogleUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val requestPasswordResetUseCase: RequestPasswordResetUseCase,
    private val saveTokensUseCase: SaveTokensUseCase,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase,
    private val userPreferences: UserPreferences,
    private val userSession: UserSession,
    private val safeCall: SafeCall,
    private val globalUIManager: GlobalUIManager,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    private val _passwordResetUiState = MutableStateFlow<PasswordResetUiState>(PasswordResetUiState.Idle)
    val passwordResetUiState: StateFlow<PasswordResetUiState> = _passwordResetUiState.asStateFlow()

    init {
        tryAutoLogin()
    }

    fun beginSignInGoogle(activity: Activity) {
        viewModelScope.launch {
            try {
                val credentialManager = CredentialManager.create(context)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                val result = credentialManager.getCredential(activity, request)
                processGoogleCredential(result)
            } catch (_: GetCredentialCancellationException) {
                // User dismissed the credential selector — not an error
            } catch (_: GetCredentialException) {
                onLoginFailed(ErrorType.GOOGLE_SIGN_IN_FAILED)
            } catch (_: Exception) {
                onLoginFailed(ErrorType.GOOGLE_LOGIN_EXCEPTION)
            }
        }
    }

    private suspend fun processGoogleCredential(result: GetCredentialResponse) {
        val credential = result.credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val response = safeCall.safeCall { googleUseCase(googleCredential.idToken) }
            if (response.isSuccess) {
                val tokens = response.getOrNull()
                if (tokens != null) {
                    saveTokensUseCase(tokens.accessToken, tokens.refreshToken)
                    userSession.login(User(email = googleCredential.id))
                    _uiState.value = AuthUiState.Success
                } else {
                    globalUIManager.setError(ErrorType.MISSING_TOKENS)
                    _uiState.value = AuthUiState.Idle
                }
            } else {
                _uiState.value = AuthUiState.Idle
            }
        } else {
            onLoginFailed(ErrorType.GOOGLE_SIGN_IN_NO_TOKEN)
        }
    }

    fun onLoginFailed(errorType: ErrorType) {
        globalUIManager.setError(errorType)
        _uiState.value = AuthUiState.Idle
    }

    private fun tryAutoLogin() {
        viewModelScope.launch {
            val hasSession = getRefreshTokenUseCase() != null
            if (hasSession) {
                globalUIManager.withLoading {
                    val response = refreshTokenUseCase()
                    _uiState.value = if (response.isSuccess) {
                        userSession.login(User(email = userPreferences.userEmailFlow.firstOrNull()))
                        AuthUiState.Success
                    } else {
                        AuthUiState.Idle
                    }
                }
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.AttemptingAuth
            val response = safeCall.safeCall { authUseCase(email, password) }
            if (response.isSuccess) {
                val tokens = response.getOrNull()
                if (tokens != null) {
                    saveTokensUseCase(tokens.accessToken, tokens.refreshToken)
                    userPreferences.saveUser(email)
                    userSession.login(User(email = email))
                    _uiState.value = AuthUiState.Success
                } else {
                    globalUIManager.setError(ErrorType.MISSING_TOKENS)
                    _uiState.value = AuthUiState.Idle
                }
            } else {
                _uiState.value = AuthUiState.Idle
            }
        }
    }

    fun registerUser(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.AttemptingAuth

            if (password != confirmPassword) {
                globalUIManager.setError(ErrorType.PASSWORD_NOT_EQUALS)
                _uiState.value = AuthUiState.Idle
                return@launch
            }

            val response = safeCall.safeCall { registerUseCase(email, password) }
            if (response.isSuccess) {
                val tokens = response.getOrNull()
                if (tokens != null) {
                    saveTokensUseCase(tokens.accessToken, tokens.refreshToken)
                    userPreferences.saveUser(email)
                    userSession.login(User(email = tokens.email ?: email))
                    _uiState.value = AuthUiState.Success
                } else {
                    globalUIManager.setError(ErrorType.MISSING_TOKENS)
                    _uiState.value = AuthUiState.Idle
                }
            } else {
                _uiState.value = AuthUiState.Idle
            }
        }
    }

    fun requestPasswordReset(email: String) {
        viewModelScope.launch {
            _passwordResetUiState.value = PasswordResetUiState.Attempting
            val response = safeCall.safeCall { requestPasswordResetUseCase(email) }
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
