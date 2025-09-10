package es.virtualclubs.presentation.handlers

import androidx.compose.runtime.Composable
import es.virtualclubs.R

object ErrorHandler {
    @Composable
    fun getErrorMessage(key: String): Int {
        return when (key) {
            "google_login_launch_failed" -> R.string.google_login_launch_failed
            "google_login_sign-in_failed" -> R.string.google_login_sign_in_failed
            "missing_tokens" -> R.string.missing_tokens
            "unknown_error" -> R.string.unknown_error
            "google_login_no_token" -> R.string.google_login_no_token
            "google_login_api_exception" -> R.string.google_login_api_exception
            "passwords_not_equals" -> R.string.passwords_not_equals
            "cant_connect_server" -> R.string.cant_connect_server
            "server_error" -> R.string.server_error
            "invalid_credentials" -> R.string.invalid_credentials
            "internal_error" -> R.string.internal_error
            "user_already_exists" -> R.string.user_already_exists
            "invalid_or_expired_refresh_session" -> R.string.invalid_or_expired_refresh_session
            "invalid_google_token" -> R.string.invalid_google_token
            "password_required" -> R.string.password_required
            "password_min_length" -> R.string.password_min_length
            "email_required" -> R.string.email_required
            "email_valid" -> R.string.email_valid
            else -> R.string.unknown_error
        }
    }
}