package es.virtualclubs.presentation.handlers

import androidx.compose.runtime.Composable
import es.virtualclubs.R
import es.virtualclubs.domain.model.ErrorType

object ErrorHandler {
  @Composable
  fun getErrorMessage(errorCode: ErrorType): Int {
    return when (errorCode) {
      ErrorType.INTERNAL_ERROR -> R.string.internal_error
      ErrorType.INVALID_CREDENTIALS -> R.string.invalid_credentials
      ErrorType.USERNAME_NOT_FOUND -> R.string.username_not_found
      ErrorType.EMAIL_ALREADY_EXISTS -> R.string.user_already_exists
      ErrorType.EMAIL_REQUIRED -> R.string.email_required
      ErrorType.PASSWORD_REQUIRED -> R.string.password_required
      ErrorType.USERNAME_REQUIRED -> R.string.username_required
      ErrorType.PASSWORD_MIN_LENGTH_ERROR -> R.string.password_min_length
      ErrorType.INVALID_EMAIL_FORMAT -> R.string.email_valid
      ErrorType.FIELD_NULL -> R.string.field_null
      ErrorType.TOKEN_BLANK -> R.string.token_blank
      ErrorType.INVALID_REFRESH_TOKEN -> R.string.invalid_or_expired_refresh_session
      ErrorType.INVALID_GOOGLE_TOKEN -> R.string.invalid_google_token
      ErrorType.FAILED_SEND_EMAIL -> R.string.failed_send_email
      ErrorType.INVALID_TOKEN -> R.string.invalid_token
      ErrorType.NO_LOCAL_PROVIDER -> R.string.no_local_provider
      ErrorType.EMAIL_NOT_FOUND -> R.string.email_not_found
      ErrorType.INVALID_ACCESS_TOKEN -> R.string.invalid_access_token
      ErrorType.CANT_CONNECT_SERVER -> R.string.cant_connect_server
      ErrorType.MISSING_TOKENS -> R.string.missing_tokens
      ErrorType.GOOGLE_SIGN_IN_FAILED -> R.string.google_login_sign_in_failed
      ErrorType.PASSWORD_NOT_EQUALS -> R.string.passwords_not_equals
      ErrorType.GOOGLE_SIGN_IN_NO_TOKEN -> R.string.google_login_no_token
      ErrorType.GOOGLE_LOGIN_EXCEPTION -> R.string.google_login_api_exception
      ErrorType.USER_NOT_FOUND -> R.string.user_not_found
      else -> R.string.unknown_error
    }
  }
}