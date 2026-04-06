package es.virtualclubs.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import es.virtualclubs.data.managers.GlobalUIManager

object AppNavigator {
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
    GlobalUIManager.clearError() // Reset the errors in new screen
    navController?.popBackStack()
  }

  fun navigateToSettings() {
    navigate(Screen.Settings.route) {
      popUpTo(Screen.Auth.route) { inclusive = true }
    }
  }

  fun navigate(route: String, builder: (NavOptionsBuilder.() -> Unit)) {
    GlobalUIManager.clearError() // Reset the errors in new screen
    navController?.navigate(route, builder)
  }
}