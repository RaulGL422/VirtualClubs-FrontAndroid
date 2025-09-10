package es.virtualclubs

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import es.virtualclubs.presentation.navigation.AppNavHost
import es.virtualclubs.presentation.navigation.AppNavigator
import es.virtualclubs.presentation.navigation.AppNavigatorImpl


@Composable
fun VirtualClubsMainApp(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController()
) {
    // Create the screen type
    val screenType = when (windowSize) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> ScreenType.Small
        WindowWidthSizeClass.Expanded -> ScreenType.Medium
        else -> ScreenType.Medium
    }

    // Initialize Nav Host
    val navigator: AppNavigatorImpl = hiltViewModel()
    AppNavHost(navController, screenType, navigator)
}

// Enum for screen types based on window size.
enum class ScreenType {
    Small,
    Medium
}