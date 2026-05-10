package es.virtualclubs.data.session

import es.virtualclubs.domain.model.SessionState
import es.virtualclubs.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class UserSession @Inject constructor() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.LoggedOut)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    @Volatile var cachedAccessToken: String? = null
        private set

    fun login(user: User) {
        _sessionState.value = SessionState.LoggedIn(user)
    }

    fun logout() {
        _sessionState.value = SessionState.LoggedOut
        cachedAccessToken = null
    }

    fun cacheAccessToken(token: String?) {
        cachedAccessToken = token
    }
}
