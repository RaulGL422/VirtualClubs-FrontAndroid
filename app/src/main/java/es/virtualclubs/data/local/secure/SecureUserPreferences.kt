package es.virtualclubs.data.local.secure

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

private const val FILE_NAME = "secure_user_prefs"

@Singleton
class SecureUserPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = FILE_NAME
    )

    private val ACCESS_KEY = stringPreferencesKey("access_token")
    private val REFRESH_KEY = stringPreferencesKey("refresh_token")

    suspend fun saveAccessToken(token: String) {
        val encrypted = EncryptionUtils.encrypt(token)
        context.dataStore.edit { prefs ->
            prefs[ACCESS_KEY] = android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT)
        }
    }

    suspend fun saveRefreshToken(token: String) {
        val encrypted = EncryptionUtils.encrypt(token)
        context.dataStore.edit { prefs ->
            prefs[REFRESH_KEY] = android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT)
        }
    }

    val accessToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[ACCESS_KEY]?.let {
            EncryptionUtils.decrypt(android.util.Base64.decode(it, android.util.Base64.DEFAULT))
        }
    }

    val refreshToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[REFRESH_KEY]?.let {
            EncryptionUtils.decrypt(android.util.Base64.decode(it, android.util.Base64.DEFAULT))
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
