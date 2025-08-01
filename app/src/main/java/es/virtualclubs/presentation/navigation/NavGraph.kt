package es.virtualclubs.presentation.navigation

import androidx.compose.runtime.Composable
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
) {
    val navigateToSettings = { navController.navigate(Screen.Settings.route) }
    val navigateBack: () -> Unit = { navController.popBackStack() }

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            LoginPage(
                onSettingsTap = navigateToSettings
            )
        }

//        composable(Screen.Settings.route) {
//            SettingsPage(
//                title = SettingsDestination.titleRes,
//                onNavigateBack = navigateBack,
//                screenType = screenType
//            )
//        }

        composable(Screen.Home.route) {
            HomePage()
        }
    }
}