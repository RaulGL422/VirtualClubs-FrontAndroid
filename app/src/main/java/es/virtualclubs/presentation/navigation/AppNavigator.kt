package es.virtualclubs.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import es.virtualclubs.data.managers.ErrorManager

object AppNavigator {
  private var navController: NavController? = null

  fun setNavController(controller: NavController) {
    navController = controller
  }

  fun navigateToLoginAndClearStack() {
    navigate(Screen.Auth.route) {
      popUpTo(0) { inclusive = true }
    }
  }

  fun navigateToLoginAndClearStackWithMessage(message: String) {
    val routeWithToken = Screen.Auth.route.replace("{message}", message)
    navigate(routeWithToken) {
      popUpTo(0) { inclusive = true }
    }
  }

  fun navigateToHome() {
    navigate(Screen.Home.route) {
      popUpTo(Screen.Auth.route) { inclusive = true }
    }
  }

  fun navigateBack() {
    ErrorManager.clearError() // Reset the errors in new screen
    navController?.popBackStack()
  }

  fun navigateToSettings() {
    navigate(Screen.Settings.route) {
      popUpTo(Screen.Auth.route) { inclusive = true }
    }
  }

  fun navigateToVerifyEmail() {
    navigate(Screen.VerifyEmail.route) {
      popUpTo(0) { inclusive = true }
    }
  }

  fun navigate(route: String, builder: (NavOptionsBuilder.() -> Unit)) {
    ErrorManager.clearError() // Reset the errors in new screen
    navController?.navigate(route, builder)
  }
}