package es.virtualclubs.data.managers

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject

class AuthInterceptor(
  private val tokenProvider: () -> String?,
  private val onTokenExpired: (() -> Unit)? = null
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val token = tokenProvider()

    if (token != null && isTokenExpiredLocally(token)) {
      try {
        onTokenExpired?.invoke()
      } catch (_: Exception) {
        // Refresh proactivo fallido: SafeResponse maneja el 401 como fallback
      }
    }

    val requestBuilder = chain.request().newBuilder()
    tokenProvider()?.let { freshToken ->
      requestBuilder.addHeader("Authorization", "Bearer $freshToken")
    }

    return chain.proceed(requestBuilder.build())
  }

  private fun isTokenExpiredLocally(token: String): Boolean {
    return try {
      val payload = token.split(".").getOrNull(1) ?: return true
      val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
      val exp = JSONObject(String(decoded)).optLong("exp", 0L)
      // Refresh si ya expiró o expira en menos de 30 segundos
      System.currentTimeMillis() / 1000 >= exp - 30
    } catch (_: Exception) {
      false
    }
  }
}
