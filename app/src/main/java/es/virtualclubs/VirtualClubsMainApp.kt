package es.virtualclubs

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import dagger.hilt.android.EntryPointAccessors
import es.virtualclubs.di.GlobalUIEntryPoint
import es.virtualclubs.presentation.managers.LocalGlobalUIManager
import es.virtualclubs.presentation.navigation.AppNavHost

@Composable
fun VirtualClubsMainApp(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController
) {
    val context = LocalContext.current
    val entryPoint = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            GlobalUIEntryPoint::class.java
        )
    }
    val globalUIManager = entryPoint.globalUIManager()
    val appNavigator = entryPoint.appNavigator()

    val screenType = when (windowSize) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> ScreenType.Small
        else -> ScreenType.Medium
    }

    CompositionLocalProvider(LocalGlobalUIManager provides globalUIManager) {
        AppNavHost(navController, screenType, appNavigator)
    }
}
