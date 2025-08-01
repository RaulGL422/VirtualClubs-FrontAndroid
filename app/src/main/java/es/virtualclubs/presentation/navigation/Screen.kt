package es.virtualclubs.presentation.navigation

sealed class Screen(val route: String, val name: String) {
    object Auth : Screen("auth", "authentication_page")
    object Settings : Screen("settings", "settings_page")
    object Home: Screen("home", "home_page")
}