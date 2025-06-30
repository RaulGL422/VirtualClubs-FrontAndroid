package es.virtualclubs.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    // Preference keys
    private object Keys {
        val EMAIL = stringPreferencesKey("user_email")
        val PASSWORD = stringPreferencesKey("user_passwd")
    }

    // Flow to observe user email
    val userEmailFlow: Flow<String?> = dataStore.data
        .map { prefs -> prefs[Keys.EMAIL] }

    // Flow to observe user password
    val userPasswordFlow: Flow<String?> = dataStore.data
        .map { prefs -> prefs[Keys.PASSWORD] }

    // Save email and password
    suspend fun saveUser(email: String, password: String) {
        dataStore.edit { prefs ->
            prefs[Keys.EMAIL] = email
            prefs[Keys.PASSWORD] = password
        }
    }

    // Clear stored user data
    suspend fun clearUser() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.EMAIL)
            prefs.remove(Keys.PASSWORD)
        }
    }
}