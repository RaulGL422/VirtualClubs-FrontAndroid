package es.virtualclubs.domain.model

enum class ErrorType(val code: Int) {
  // Códigos 1-13: alineados con el backend (fuente de verdad)
  INTERNAL_ERROR(1),
  INVALID_CREDENTIALS(2),
  INVALID_REFRESH_TOKEN(3),
  INVALID_TOKEN(4),
  USER_NOT_FOUND(5),
  EMAIL_ALREADY_EXISTS(6),
  FIELD_BLANK(7),
  INVALID_EMAIL(8),
  PASSWORD_TOO_SHORT(9),
  PASSWORD_TOO_WEAK(10),
  EMAIL_NOT_VERIFIED(11),
  NO_LOCAL_PROVIDER(12),
  RATE_LIMIT_EXCEEDED(13),
  // Códigos 14+: errores solo de Android sin equivalente en el backend
  FAILED_SEND_EMAIL(14),
  EMAIL_NOT_FOUND(15),
  INVALID_ACCESS_TOKEN(16),
  CANT_CONNECT_SERVER(17),
  MISSING_TOKENS(18),
  GOOGLE_SIGN_IN_FAILED(19),
  PASSWORD_NOT_EQUALS(20),
  GOOGLE_SIGN_IN_NO_TOKEN(21),
  GOOGLE_LOGIN_EXCEPTION(22),
  INVALID_GOOGLE_TOKEN(23),
  USERNAME_NOT_FOUND(24),
  EMAIL_REQUIRED(25),
  PASSWORD_REQUIRED(26),
  USERNAME_REQUIRED(27);

  companion object {
    @JvmStatic
    fun fromCode(code: Int): ErrorType {
      return entries.find { it.code == code } ?: INTERNAL_ERROR
    }
  }
}