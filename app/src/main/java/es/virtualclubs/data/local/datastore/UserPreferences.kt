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

    companion object {
        private val EMAIL = stringPreferencesKey("email")
    }

    val userEmailFlow: Flow<String?> = dataStore.data
        .map { prefs -> prefs[EMAIL] }

    suspend fun saveUser(email: String) {
        dataStore.edit { prefs ->
            prefs[EMAIL] = email
        }
    }

    suspend fun clearUser() {
        dataStore.edit { prefs ->
            prefs.remove(EMAIL)
        }
    }
}