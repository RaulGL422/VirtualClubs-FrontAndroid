package es.virtualclubs.presentation.navigation

import androidx.navigation.NavController
import jakarta.inject.Inject
import jakarta.inject.Singleton

class AppNavigatorImpl @Inject constructor() : AppNavigator {

    private var navController: NavController? = null

    fun setNavController(controller: NavController) {
        navController = controller
    }

    override fun navigateToLoginAndClearStack() {
        navController?.navigate(Screen.Auth.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    override fun navigateToHome() {
        navController?.navigate(Screen.Home.route) {
            popUpTo(Screen.Auth.route) { inclusive = true }
        }
    }

    override fun navigateBack() {
        navController?.popBackStack()
    }

    override fun navigateToSettings() {
        navController?.navigate(Screen.Settings.route) {
            popUpTo(Screen.Auth.route) { inclusive = true }
        }
    }
}