package es.virtualclubs.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import es.virtualclubs.presentation.managers.GlobalUIManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton inyectable que centraliza la navegación de la app.
 *
 * El [NavController] se registra desde [AppNavHost] mediante [setNavController].
 * Las funciones de navegación limpian el estado de error automáticamente antes de navegar.
 */
@Singleton
class AppNavigator @Inject constructor(
    private val globalUIManager: dagger.Lazy<GlobalUIManager>
) {
    private var navController: NavController? = null

    fun setNavController(controller: NavController) {
        navController = controller
    }

    fun navigateToLoginAndClearStack() {
        navigate(Screen.Auth.route) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateToLoginAndClearStackWithMessage(message: String) {
        val routeWithToken = Screen.Auth.route.replace("{message}", message)
        navigate(routeWithToken) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateToHome() {
        navigate(Screen.Home.route) {
            popUpTo(Screen.Auth.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateBack() {
        globalUIManager.get().clearError()
        navController?.popBackStack()
    }

    fun navigateToSettings() {
        navigate(Screen.Settings.route) {
            popUpTo(Screen.Auth.route) { inclusive = true }
        }
    }

    fun navigate(route: String, builder: (NavOptionsBuilder.() -> Unit)) {
        globalUIManager.get().clearError()
        navController?.navigate(route, builder)
    }
}
