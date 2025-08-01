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

    // Preference keys
    companion object {
        private val EMAIL = stringPreferencesKey("email")
        private val AUTOLOGIN = booleanPreferencesKey("autologin")
    }

    // Flow to observe user email
    val userEmailFlow: Flow<String?> = dataStore.data
        .map { prefs -> prefs[EMAIL] }

    // Flow to observe user autologin
    val autoLoginFlow: Flow<Boolean?> = dataStore.data
        .map { prefs -> prefs[AUTOLOGIN] }

    // Save email and autologin
    suspend fun saveUser(email: String, autologin: Boolean) {
        dataStore.edit { prefs ->
            prefs[EMAIL] = email
            prefs[AUTOLOGIN] = autologin
        }
    }

    // Clear stored user data
    suspend fun clearUser() {
        dataStore.edit { prefs ->
            prefs.remove(EMAIL)
            prefs.remove(AUTOLOGIN)
        }
    }
}