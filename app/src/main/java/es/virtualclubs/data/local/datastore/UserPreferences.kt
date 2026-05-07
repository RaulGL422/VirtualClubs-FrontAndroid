package es.virtualclubs.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val EMAIL = stringPreferencesKey("email")
        // Removed in favour of refresh-token-based session detection; cleaned up on next save/clear
        private val LEGACY_AUTOLOGIN = booleanPreferencesKey("autologin")
    }

    val userEmailFlow: Flow<String?> = dataStore.data
        .map { prefs -> prefs[EMAIL] }

    suspend fun saveUser(email: String) {
        dataStore.edit { prefs ->
            prefs[EMAIL] = email
            prefs.remove(LEGACY_AUTOLOGIN)
        }
    }

    suspend fun clearUser() {
        dataStore.edit { prefs ->
            prefs.remove(EMAIL)
            prefs.remove(LEGACY_AUTOLOGIN)
        }
    }
}