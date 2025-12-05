package es.virtualclubs.presentation.navigation

sealed class Screen(val route: String, val name: String) {
    object Auth : Screen("auth/{message}", "authentication_page")
    object Settings : Screen("settings", "settings_page")
    object ResetPassword : Screen("reset-password/{token}", "reset_password_page")
    object VerifyEmail : Screen("verify_email", "verify_email_page")
    object VerifyEmailResult : Screen("verify_email_result/{token}", "verify_email_result_page")
    object Home: Screen("home", "home_page")
}