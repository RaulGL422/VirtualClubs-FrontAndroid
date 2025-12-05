package es.virtualclubs

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import es.virtualclubs.presentation.navigation.AppNavHost

@Composable
fun VirtualClubsMainApp(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController
) {
    val screenType = when (windowSize) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> ScreenType.Small
        else -> ScreenType.Medium
    }

    AppNavHost(navController, screenType)
}

// Enum for screen types based on window size.
enum class ScreenType {
    Small,
    Medium
}