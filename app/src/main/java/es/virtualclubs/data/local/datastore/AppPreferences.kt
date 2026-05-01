package es.virtualclubs.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    // Preference keys
    companion object {
        private val DARK_THEME = booleanPreferencesKey("dark_theme")
        private val CONTRAST_TYPE = intPreferencesKey("contrast_type")
        private val FONT_SIZE_MULTIPLIER = doublePreferencesKey("font_size_multiplier")
        private val DEBUG_SERVER_URL = stringPreferencesKey("debug_server_url")
    }

    // ------------------- Theme -------------------

    val darkThemeFlow: Flow<Boolean?> = dataStore.data
        .map { it[DARK_THEME] }

    suspend fun saveThemeStyle(isDark: Boolean?) {
        dataStore.edit { prefs ->
            if (isDark == null) {
                prefs.remove(DARK_THEME)
            } else {
                prefs[DARK_THEME] = isDark
            }
        }
    }

    // ------------------- Contrast -------------------

    val contrastTypeFlow: Flow<Int> = dataStore.data
        .map { it[CONTRAST_TYPE] ?: 0 }

    suspend fun saveContrastType(value: Int) {
        dataStore.edit { it[CONTRAST_TYPE] = value }
    }

    // ------------------- Font Size -------------------

    val fontSizeMultiplierFlow: Flow<Double> = dataStore.data
        .map { it[FONT_SIZE_MULTIPLIER] ?: 1.0 }

    suspend fun saveFontSizeMultiplier(value: Double) {
        dataStore.edit { it[FONT_SIZE_MULTIPLIER] = value }
    }

    // ------------------- Debug server URL (solo debug builds) -------------------

    val debugServerUrlFlow: Flow<String> = dataStore.data
        .map { it[DEBUG_SERVER_URL] ?: "" }

    suspend fun setDebugServerUrl(url: String) {
        dataStore.edit { it[DEBUG_SERVER_URL] = url }
    }
}
