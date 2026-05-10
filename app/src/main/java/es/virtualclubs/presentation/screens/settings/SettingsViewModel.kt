package es.virtualclubs.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.virtualclubs.data.local.datastore.AppPreferences
import es.virtualclubs.domain.model.SessionState
import es.virtualclubs.domain.usecase.GetSessionStateUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val getSessionState: GetSessionStateUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _restartSignal = Channel<Unit>(Channel.BUFFERED)
    val restartSignal = _restartSignal.receiveAsFlow()

    init {
        viewModelScope.launch {
            val prefsFlow = combine(
                appPreferences.darkThemeFlow,
                appPreferences.contrastTypeFlow,
                appPreferences.fontSizeMultiplierFlow,
                appPreferences.notificationsEnabledFlow,
                appPreferences.appLanguageFlow
            ) { dark, contrast, fontSize, notifications, language ->
                PrefsSnapshot(dark, contrast, fontSize, notifications, language)
            }

            combine(
                prefsFlow,
                getSessionState(),
                appPreferences.debugServerUrlFlow
            ) { prefs, sessionState, debugUrl ->
                SettingsUiState(
                    isDarkTheme = prefs.isDarkTheme,
                    contrastType = prefs.contrastType,
                    fontSizeMultiplier = prefs.fontSizeMultiplier,
                    notificationsEnabled = prefs.notificationsEnabled,
                    appLanguage = prefs.appLanguage,
                    email = (sessionState as? SessionState.LoggedIn)?.user?.email,
                    debugServerUrl = debugUrl
                )
            }.collect { _uiState.value = it }
        }
    }

    fun setTheme(isDark: Boolean?) {
        viewModelScope.launch { appPreferences.saveThemeStyle(isDark) }
    }

    fun setContrast(value: Int) {
        viewModelScope.launch { appPreferences.saveContrastType(value) }
    }

    fun setFontSize(value: Double) {
        viewModelScope.launch { appPreferences.saveFontSizeMultiplier(value) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { appPreferences.setNotificationsEnabled(enabled) }
    }

    fun setAppLanguage(tag: String) {
        viewModelScope.launch { appPreferences.setAppLanguage(tag) }
    }

    fun saveDebugServerUrl(url: String) {
        viewModelScope.launch {
            appPreferences.setDebugServerUrl(url.trim())
            _restartSignal.send(Unit)
        }
    }
}

private data class PrefsSnapshot(
    val isDarkTheme: Boolean?,
    val contrastType: Int,
    val fontSizeMultiplier: Double,
    val notificationsEnabled: Boolean,
    val appLanguage: String
)

data class SettingsUiState(
    val isDarkTheme: Boolean? = null,
    val contrastType: Int = 0,
    val fontSizeMultiplier: Double = 1.0,
    val notificationsEnabled: Boolean = true,
    val appLanguage: String = "",
    val email: String? = null,
    val debugServerUrl: String = ""
) {
    val isLoggedIn: Boolean get() = email != null
}
