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
    private object Keys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val CONTRAST_TYPE = intPreferencesKey("contrast_type")
        val FONT_SIZE_MULTIPLIER = doublePreferencesKey("font_size_multiplier")
    }

    // ------------------- Theme -------------------

    val darkThemeFlow: Flow<Boolean?> = dataStore.data
        .map { it[Keys.DARK_THEME] }

    suspend fun saveThemeStyle(isDark: Boolean?) {
        dataStore.edit { prefs ->
            if (isDark == null) {
                prefs.remove(Keys.DARK_THEME)
            } else {
                prefs[Keys.DARK_THEME] = isDark
            }
        }
    }

    // ------------------- Contrast -------------------

    val contrastTypeFlow: Flow<Int> = dataStore.data
        .map { it[Keys.CONTRAST_TYPE] ?: 0 }

    suspend fun saveContrastType(value: Int) {
        dataStore.edit { it[Keys.CONTRAST_TYPE] = value }
    }

    // ------------------- Font Size -------------------

    val fontSizeMultiplierFlow: Flow<Double> = dataStore.data
        .map { it[Keys.FONT_SIZE_MULTIPLIER] ?: 1.0 }

    suspend fun saveFontSizeMultiplier(value: Double) {
        dataStore.edit { it[Keys.FONT_SIZE_MULTIPLIER] = value }
    }
}