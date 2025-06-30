package es.iesfernandoaguilar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.iesfernandoaguilar.models.ViewModelControl
import es.iesfernandoaguilar.models.objects.ViewModelActive
import es.virtualclubs.data.local.datastore.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel(), ViewModelControl {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        ViewModelActive.setActive(this)

        // Observe and load settings from preferences
        viewModelScope.launch {
            combine(
                appPreferences.darkThemeFlow,
                appPreferences.contrastTypeFlow,
                appPreferences.fontSizeMultiplierFlow,
                appPreferences.serverIpFlow
            ) { darkTheme, contrast, fontSize, ip ->
                SettingsState(
                    darkTheme = darkTheme,
                    contrastType = contrast,
                    fontSizeMultiplier = fontSize,
                    serverIp = ip
                )
            }.collectLatest { newState ->
                _state.value = newState
            }
        }
    }

    // Change theme and persist it
    fun changeTheme(isDarkTheme: Boolean?) {
        updateStateAndPreferences(
            update = { it.copy(darkTheme = isDarkTheme) },
            persist = { appPreferences.saveThemeStyle(isDarkTheme) }
        )
    }

    // Change contrast level and persist it
    fun changeContrast(value: Int) {
        updateStateAndPreferences(
            update = { it.copy(contrastType = value) },
            persist = { appPreferences.saveContrastType(value) }
        )
    }

    // Change font size multiplier and persist it
    fun changeFontSize(value: Double) {
        updateStateAndPreferences(
            update = { it.copy(fontSizeMultiplier = value) },
            persist = { appPreferences.saveFontSizeMultiplier(value) }
        )
    }

    // Change server IP and persist it
    fun changeIp(ip: String) {
        updateStateAndPreferences(
            update = { it.copy(serverIp = ip) },
            persist = { appPreferences.saveServerIp(ip) }
        )
    }

    // Helper function to reduce repeated state & preference update logic
    private fun updateStateAndPreferences(
        update: (SettingsState) -> SettingsState,
        persist: suspend () -> Unit
    ) {
        _state.value = update(_state.value)
        viewModelScope.launch { persist() }
    }
}

// Holds all current settings state for the UI
data class SettingsState(
    val darkTheme: Boolean? = null,
    val contrastType: Int = 0,
    val fontSizeMultiplier: Double = 1.0,
    val serverIp: String = ""
)