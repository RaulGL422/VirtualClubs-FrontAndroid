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
import es.virtualclubs.presentation.screens.verifyemailresult.VerifyEmailResultPage
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
        titlePage = Screen.ResetPassword.nameId,
        token = token,
        screenType = screenType,
        onSettingsTap = { AppNavigator.navigateToSettings() },
        onBack = { AppNavigator.navigateBack() },
        onPasswordResetSuccess = { AppNavigator.navigateToLoginAndClearStackWithMessage(it) }
      )
    }

    composable(
      route = Screen.VerifyEmailResult.route,
      arguments = listOf(navArgument("status") { type = NavType.StringType }),
      deepLinks = listOf(
        navDeepLink {
          uriPattern = "virtualclubs://email/verify-email?status={status}"
        }
      )
    ) {
      VerifyEmailResultPage(
        onGoToLogin = { AppNavigator.navigateToLoginAndClearStack() },
        onSettingsTap = { AppNavigator.navigateToSettings() }
      )
    }
  }
}