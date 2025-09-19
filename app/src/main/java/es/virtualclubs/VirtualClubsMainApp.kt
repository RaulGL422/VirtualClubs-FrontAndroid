package es.virtualclubs

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
    navController: NavHostController = rememberNavController(),
    deepLinkData: android.net.Uri? = null
) {
    val screenType = when (windowSize) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> ScreenType.Small
        WindowWidthSizeClass.Expanded -> ScreenType.Medium
        else -> ScreenType.Medium
    }

    val navigator = AppNavigatorImpl()

    LaunchedEffect(deepLinkData) {
        deepLinkData?.let { uri ->
            when (uri.path) {
                "/reset-password" -> {
                    val token = uri.getQueryParameter("token")
                    token?.let { navigator.navigateToResetPassword(it) }
                }
                "/verify-email" -> {
                    val token = uri.getQueryParameter("token")
                    token?.let { navigator.navigateToVerifyEmail(it) }
                }
            }
        }
    }

    AppNavHost(navController, screenType, navigator)
}

// Enum for screen types based on window size.
enum class ScreenType {
    Small,
    Medium
}