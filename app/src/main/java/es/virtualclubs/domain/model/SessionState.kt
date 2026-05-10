package es.virtualclubs.domain.model

sealed class SessionState {
    data object LoggedOut : SessionState()
    data class LoggedIn(val user: User) : SessionState()
}
