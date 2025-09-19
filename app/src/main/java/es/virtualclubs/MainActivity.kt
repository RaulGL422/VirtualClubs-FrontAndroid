package es.virtualclubs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import es.virtualclubs.data.local.datastore.AppPreferences
import es.virtualclubs.presentation.theme.VirtualClubsTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appPreferences: AppPreferences

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val deepLinkData = intent?.data
        setContent {
            VirtualClubsTheme(
                preferences = appPreferences
            ) {
                val windowSize = calculateWindowSizeClass(this)
                VirtualClubsMainApp(
                    windowSize.widthSizeClass,
                    deepLinkData = deepLinkData
                )
            }
        }
    }
}