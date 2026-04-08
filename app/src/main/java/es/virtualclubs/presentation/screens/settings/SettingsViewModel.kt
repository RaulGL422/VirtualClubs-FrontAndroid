package es.virtualclubs.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.virtualclubs.data.local.datastore.AppPreferences
import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.managers.GlobalUIManager
import es.virtualclubs.domain.usecase.LogoutUserUseCase
import es.virtualclubs.domain.usecase.token.ClearTokensUseCase
import es.virtualclubs.presentation.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val appPreferences: AppPreferences,
  private val userPreferences: UserPreferences,
  private val logoutUseCase: LogoutUserUseCase,
  private val clearTokensUseCase: ClearTokensUseCase
) : ViewModel() {

  private val _uiState = MutableStateFlow(SettingsUiState())
  val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      combine(
        appPreferences.darkThemeFlow,
        appPreferences.contrastTypeFlow,
        appPreferences.fontSizeMultiplierFlow,
        userPreferences.userEmailFlow
      ) { dark, contrast, fontSize, email ->
        SettingsUiState(
          isDarkTheme = dark,
          contrastType = contrast,
          fontSizeMultiplier = fontSize,
          email = email
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

  fun logout() {
    viewModelScope.launch {
      GlobalUIManager.withLoading {
        logoutUseCase()
        clearTokensUseCase()
        AppNavigator.navigateToLoginAndClearStack()
      }
    }
  }
}

data class SettingsUiState(
  val isDarkTheme: Boolean? = null,
  val contrastType: Int = 0,
  val fontSizeMultiplier: Double = 1.0,
  val email: String? = null
)
