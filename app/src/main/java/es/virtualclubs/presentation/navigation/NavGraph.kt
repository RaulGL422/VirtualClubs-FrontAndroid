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
import es.virtualclubs.presentation.screens.settings.SettingsPage
import es.virtualclubs.presentation.screens.splash.SplashPage
import es.virtualclubs.presentation.screens.verifyemailresult.VerifyEmailResultPage
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavHost(
    navController: NavHostController,
    screenType: ScreenType,
    appNavigator: AppNavigator
) {
    LaunchedEffect(navController) {
        appNavigator.setNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashPage(
                onNavigateToHome = { appNavigator.navigateToHomeAndClearStack() },
                onNavigateToLogin = { appNavigator.navigateToLoginAndClearStack() }
            )
        }

        composable(Screen.Auth.route) { backStackEntry ->
            val message = backStackEntry.arguments?.getString("message")
            LoginPage(
                onSettingsTap = { appNavigator.navigateToSettings() },
                screenType = screenType,
                message = message,
                onLogged = { appNavigator.navigateToHome() }
            )
        }

        composable(Screen.Home.route) {
            HomePage()
        }

        composable(Screen.Settings.route) {
            SettingsPage(
                screenType = screenType,
                onBack = { appNavigator.navigateBack() }
            )
        }

        composable(
            route = Screen.ResetPassword.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType }),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "virtualclubs://pass/resetPassword?token={token}"
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
                onSettingsTap = { appNavigator.navigateToSettings() },
                onPasswordResetSuccess = { appNavigator.navigateToLoginAndClearStackWithMessage(it) }
            )
        }

        composable(
            route = Screen.VerifyEmailResult.route,
            arguments = listOf(navArgument("status") { type = NavType.StringType }),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "virtualclubs://email/verifyEmail?status={status}"
                }
            )
        ) {
            VerifyEmailResultPage(
                onGoToHome = { appNavigator.navigateToSplashAndClearStack() },
                onSettingsTap = { appNavigator.navigateToSettings() },
                screenType = screenType
            )
        }
    }
}
