package es.virtualclubs.data.local.secure

import android.content.Context
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.UserNotAuthenticatedException
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

  private suspend fun handleInvalidatedKey() {
    EncryptionUtils.deleteKey()
    clearAll()
  }

  suspend fun saveAccessToken(token: String) {
    try {
      val encrypted = EncryptionUtils.encrypt(token)
      context.dataStore.edit { prefs ->
        prefs[ACCESS_KEY] = android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT)
      }
    } catch (e: KeyPermanentlyInvalidatedException) {
      handleInvalidatedKey()
      throw VirtualClubException(ErrorType.MISSING_TOKENS)
    } catch (e: UserNotAuthenticatedException) {
      handleInvalidatedKey()
      throw VirtualClubException(ErrorType.MISSING_TOKENS)
    }
  }

  suspend fun saveRefreshToken(token: String) {
    try {
      val encrypted = EncryptionUtils.encrypt(token)
      context.dataStore.edit { prefs ->
        prefs[REFRESH_KEY] =
          android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT)
      }
    } catch (e: KeyPermanentlyInvalidatedException) {
      handleInvalidatedKey()
      throw VirtualClubException(ErrorType.MISSING_TOKENS)
    } catch (e: UserNotAuthenticatedException) {
      handleInvalidatedKey()
      throw VirtualClubException(ErrorType.MISSING_TOKENS)
    }
  }

  suspend fun saveTokens(accessToken: String, refreshToken: String) {
    saveAccessToken(accessToken)
    saveRefreshToken(refreshToken)
  }

  val accessToken: Flow<String?> = context.dataStore.data.map { prefs ->
    try {
      prefs[ACCESS_KEY]?.let {
        EncryptionUtils.decrypt(android.util.Base64.decode(it, android.util.Base64.DEFAULT))
      }
    } catch (e: KeyPermanentlyInvalidatedException) {
      handleInvalidatedKey()
      null
    } catch (e: UserNotAuthenticatedException) {
      handleInvalidatedKey()
      null
    }
  }

  val refreshToken: Flow<String?> = context.dataStore.data.map { prefs ->
    try {
      prefs[REFRESH_KEY]?.let {
        EncryptionUtils.decrypt(android.util.Base64.decode(it, android.util.Base64.DEFAULT))
      }
    } catch (e: KeyPermanentlyInvalidatedException) {
      handleInvalidatedKey()
      null
    } catch (e: UserNotAuthenticatedException) {
      handleInvalidatedKey()
      null
    }
  }

  suspend fun clearAll() {
    context.dataStore.edit { it.clear() }
  }
}
