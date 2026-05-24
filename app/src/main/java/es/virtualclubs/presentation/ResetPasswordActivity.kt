package es.virtualclubs.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import es.virtualclubs.BuildConfig
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import es.virtualclubs.VirtualClubsMainApp
import es.virtualclubs.data.local.datastore.AppPreferences
import es.virtualclubs.presentation.navigation.Screen
import es.virtualclubs.presentation.theme.VirtualClubsTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@AndroidEntryPoint
class ResetPasswordActivity : ComponentActivity() {
  @Inject
  lateinit var appPreferences: AppPreferences
  private var navController: NavHostController? = null

  @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()
    setContent {
      VirtualClubsTheme(preferences = appPreferences) {
        val windowSize = calculateWindowSizeClass(this)
        val controller = rememberNavController()
        navController = controller

        VirtualClubsMainApp(windowSize.widthSizeClass, navController = controller)

        LaunchedEffect(controller) {
          navigateToResetPassword(controller, intent)
        }
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    navController?.let { navigateToResetPassword(it, intent) }
  }

  private fun navigateToResetPassword(controller: NavHostController, intent: Intent) {
    val token = intent.data?.getQueryParameter("token")
      ?: if (BuildConfig.DEBUG) "preview-token" else return
    val encoded = URLEncoder.encode(token, StandardCharsets.UTF_8.toString())
    val route = Screen.ResetPassword.route.replace("{token}", encoded)
    controller.navigate(route) {
      popUpTo(Screen.Auth.route) { inclusive = true }
      launchSingleTop = true
    }
  }
}
