package es.virtualclubs

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import es.virtualclubs.data.local.datastore.AppPreferences
import es.virtualclubs.presentation.theme.VirtualClubsTheme
import javax.inject.Inject

@AndroidEntryPoint
class VerifyEmailActivity : ComponentActivity() {
  @Inject
  lateinit var appPreferences: AppPreferences
  private var navController: NavHostController? = null

  @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()
    setContent {
      VirtualClubsTheme(
        preferences = appPreferences
      ) {
        val windowSize = calculateWindowSizeClass(this)
        val controller = rememberNavController()
        navController = controller

        VirtualClubsMainApp(
          windowSize.widthSizeClass,
          navController = controller
        )

        LaunchedEffect(controller) {
          controller.handleDeepLink(intent)
        }
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    navController?.handleDeepLink(intent)
  }
}
