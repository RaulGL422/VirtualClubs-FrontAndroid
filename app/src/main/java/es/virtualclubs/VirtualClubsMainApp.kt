package es.virtualclubs

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import es.virtualclubs.presentation.navigation.AppNavHost
import es.virtualclubs.presentation.navigation.AppNavigatorImpl


@Composable
fun VirtualClubsMainApp(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController
) {
    val screenType = when (windowSize) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> ScreenType.Small
        WindowWidthSizeClass.Expanded -> ScreenType.Medium
        else -> ScreenType.Medium
    }

    val navigator = AppNavigatorImpl()

    AppNavHost(navController, screenType, navigator)
}

// Enum for screen types based on window size.
enum class ScreenType {
    Small,
    Medium
}