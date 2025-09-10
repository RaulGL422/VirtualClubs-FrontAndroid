package es.virtualclubs.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import es.virtualclubs.ScreenType
import es.virtualclubs.presentation.screens.auth.LoginPage
import es.virtualclubs.presentation.screens.home.HomePage

@Composable
fun AppNavHost(
    navController: NavHostController,
    screenType: ScreenType,
    appNavigator: AppNavigatorImpl = AppNavigatorImpl()
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
                onLogged = { appNavigator.navigateToHome() },
                onForgottedPass = { }
            )
        }

        composable(Screen.Home.route) {
            HomePage()
        }
    }
}