package es.virtualclubs.presentation.navigation

import es.virtualclubs.R

sealed class Screen(val route: String, val nameId: Int) {
  object Auth : Screen("auth/{message}", R.string.authentication_page)
  object Settings : Screen("settings", R.string.settings_page)
  object ResetPassword : Screen("reset-password/{token}", R.string.reset_password_page)
  object VerifyEmailResult : Screen("verify_email/{status}", R.string.verify_email_page)
  object Home : Screen("home", R.string.home_page)
}