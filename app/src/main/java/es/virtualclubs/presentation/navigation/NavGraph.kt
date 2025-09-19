package es.virtualclubs.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import es.virtualclubs.ScreenType
import es.virtualclubs.presentation.screens.auth.LoginPage
import es.virtualclubs.presentation.screens.home.HomePage
import es.virtualclubs.presentation.screens.resetPassword.ResetPasswordPage

@Composable
fun AppNavHost(
    navController: NavHostController,
    screenType: ScreenType,
    appNavigator: AppNavigatorImpl
) {
    LaunchedEffect(navController) {
        appNavigator.setNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            LoginPage(
                onSettingsTap = { appNavigator.navigateToSettings() },
                screenType = screenType,
                onLogged = { appNavigator.navigateToHome() }
            )
        }

        composable(Screen.Home.route) {
            HomePage()
        }

        composable(
            route = Screen.ResetPassword.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            ResetPasswordPage(token = token)
        }

        composable(
            route = Screen.VerifyEmail.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
        }
    }
}