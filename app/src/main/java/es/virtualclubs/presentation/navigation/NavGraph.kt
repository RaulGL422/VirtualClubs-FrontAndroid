package es.iesfernandoaguilar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import es.iesfernandoaguilar.models.objects.SessionData
import es.iesfernandoaguilar.ui.pages.ActiveUserDestination
import es.iesfernandoaguilar.ui.pages.ActiveUserPage
import es.iesfernandoaguilar.ui.pages.ClassDestination
import es.iesfernandoaguilar.ui.pages.ClassPage
import es.iesfernandoaguilar.ui.pages.ClubDestination
import es.iesfernandoaguilar.ui.pages.ClubPage
import es.iesfernandoaguilar.ui.pages.HomeDestination
import es.iesfernandoaguilar.ui.pages.HomePage
import es.iesfernandoaguilar.ui.pages.LoginDestination
import es.iesfernandoaguilar.ui.pages.LoginPage
import es.iesfernandoaguilar.ui.pages.SettingsDestination
import es.iesfernandoaguilar.ui.pages.SettingsPage
import es.iesfernandoaguilar.ui.pages.UserInformationDestination
import es.iesfernandoaguilar.ui.pages.UserInformationPage
import es.virtualclubs.ScreenType

@Composable
fun AppNavHost(
    navController: NavHostController,
    screenType: ScreenType,
    navViewModel: GlobalNavViewModel = hiltViewModel() // Injected ViewModel for navigation state
) {
    // Collect current navigation route from ViewModel as state
    val navRoute by navViewModel.navRoute.collectAsState()

    // React to navigation route changes and navigate accordingly
    LaunchedEffect(navRoute) {
        navRoute?.let { route ->
            navController.navigate(route)
            navViewModel.resetRoute()
        }
    }

    val navigateToSettings = { navController.navigate(SettingsDestination.route) }
    val navigateBack: () -> Unit = { navController.popBackStack() }

    NavHost(
        navController = navController,
        startDestination = LoginDestination.route
    ) {
        composable(LoginDestination.route) {
            LoginPage(onSettingsTap = navigateToSettings)
        }

        composable(SettingsDestination.route) {
            SettingsPage(
                title = SettingsDestination.titleRes,
                onNavigateBack = navigateBack,
                screenType = screenType
            )
        }

        composable(ActiveUserDestination.route) {
            ActiveUserPage(
                onSettingsTap = navigateToSettings,
                onBack = navigateBack,
                screenType = screenType
            )
        }

        composable(ClassDestination.route) {
            ClassPage(
                onSettingsPressed = navigateToSettings,
                onNavigateBack = navigateBack,
                screenType = screenType
            )
        }

        composable(UserInformationDestination.route) {
            UserInformationPage(
                onSettingsPressed = navigateToSettings,
                onLogOut = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                    }
                    navViewModel.logout()
                },
                screenType = screenType,
                title = UserInformationDestination.titleRes
            )
        }

        composable(HomeDestination.route) {
            HomePage(
                onLogOut = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                    }
                    navViewModel.logout()
                },
                onClubSelected = {
                    SessionData.clubSelected = it
                    navController.navigate(ClubDestination.route)
                },
                onSettingsPressed = navigateToSettings,
                onClassSelected = {
                    SessionData.classSelected = it
                    navController.navigate(ClassDestination.route)
                },
                title = HomeDestination.titleRes,
                screenType = screenType
            )
        }

        composable(ClubDestination.route) {
            ClubPage(
                onSettingsPressed = navigateToSettings,
                onClassSelected = {
                    SessionData.classSelected = it
                    navController.navigate(ClassDestination.route)
                },
                onNavigateBack = navigateBack,
                screenType = screenType
            )
        }
    }
}