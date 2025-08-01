package es.virtualclubs.presentation.handlers

import androidx.compose.runtime.Composable
import es.virtualclubs.R

object ErrorHandler {
    @Composable
    fun getErrorMessage(key: String): Int {
        return when (key) {
            "user_already_exists" -> R.string.user_already_exists
            else -> R.string.email
        }
    }
}