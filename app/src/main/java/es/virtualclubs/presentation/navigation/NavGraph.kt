package es.virtualclubs.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import es.virtualclubs.ScreenType
import es.virtualclubs.presentation.screens.auth.LoginPage
import es.virtualclubs.presentation.screens.home.HomePage
import es.virtualclubs.presentation.screens.resetPassword.ResetPasswordPage
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavHost(
  navController: NavHostController,
  screenType: ScreenType
) {
  LaunchedEffect(navController) {
    AppNavigator.setNavController(navController)
  }

  NavHost(
    navController = navController,
    startDestination = Screen.Auth.route
  ) {
    composable(
      Screen.Auth.route
    ) { backStackEntry ->
      val message = backStackEntry.arguments?.getString("message")
      LoginPage(
        onSettingsTap = { AppNavigator.navigateToSettings() },
        screenType = screenType,
        message = message,
        onLogged = { AppNavigator.navigateToHome() }
      )
    }

    composable(Screen.Home.route) {
      HomePage()
    }

    composable(
      route = Screen.ResetPassword.route,
      arguments = listOf(navArgument("token") { type = NavType.StringType }),
      deepLinks = listOf(
        navDeepLink {
          uriPattern = "virtualclubs://pass/reset-password?token={token}"
        }
      )
    ) { backStackEntry ->
      val token = backStackEntry.arguments?.getString("token")?.let {
        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
      } ?: ""
      ResetPasswordPage(
        token = token,
        screenType = screenType,
        onSettingsTap = { AppNavigator.navigateToSettings() },
        onBack = { AppNavigator.navigateBack() },
        onPasswordResetSuccess = { AppNavigator.navigateToLoginAndClearStackWithMessage(it) }
      )
    }

    composable(
      route = Screen.VerifyEmail.route,
    ) {
      // TODO Hacer pagina de verificar email
    }

    composable(
      route = Screen.VerifyEmailResult.route,
      arguments = listOf(navArgument("result") { type = NavType.StringType }),
      deepLinks = listOf(
        navDeepLink {
          uriPattern = "virtualclubs://verify_email_result?result={result}"
        }
      )
    ) { backStackEntry ->
      val result = backStackEntry.arguments?.getBoolean("result") ?: "0"
      // TODO Hacer pagina de obtener resultado de la verificacion
    }
  }
}