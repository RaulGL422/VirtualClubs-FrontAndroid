package es.virtualclubs.presentation.navigation

sealed class Screen(val route: String, val name: String) {
    object Auth : Screen("auth", "authentication_page")
    object Settings : Screen("settings", "settings_page")
    object ResetPassword : Screen("reset-password/{token}", "reset_password_page")
    object VerifyEmail : Screen("verify_email/{token}", "verify_email_page")
    object Home: Screen("home", "home_page")
}