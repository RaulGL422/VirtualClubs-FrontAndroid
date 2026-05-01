package es.virtualclubs.presentation.screens.settings

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.virtualclubs.BuildConfig
import es.virtualclubs.R
import kotlin.system.exitProcess
import es.virtualclubs.presentation.components.VCButton
import es.virtualclubs.presentation.components.VCButtonContent
import es.virtualclubs.presentation.components.VCButtonStyle
import es.virtualclubs.presentation.components.VCScaffold
import kotlin.math.roundToInt

@Composable
fun SettingsPage(
  viewModel: SettingsViewModel = hiltViewModel(),
  onBack: () -> Unit
) {
  val uiState by viewModel.uiState.collectAsState()
  val context = LocalContext.current

  LaunchedEffect(Unit) {
    viewModel.restartSignal.collect {
      val intent = context.packageManager
        .getLaunchIntentForPackage(context.packageName) ?: return@collect
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
      context.startActivity(intent)
      exitProcess(0)
    }
  }

  VCScaffold(
    titleTopBar = R.string.settings_page,
    canGoBack = true,
    onNavigateBack = onBack
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Spacer(Modifier.height(8.dp))

      // ----- Cuenta -----
      SectionTitle(stringResource(R.string.settings_account))

      uiState.email?.let { email ->
        Text(
          text = email,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

      // ----- Apariencia -----
      SectionTitle(stringResource(R.string.settings_appearance))

      SectionLabel(stringResource(R.string.settings_theme))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        VCButton(
          content = VCButtonContent.Text(R.string.settings_theme_dark),
          style = if (uiState.isDarkTheme == true) VCButtonStyle.Primary else VCButtonStyle.Outline,
          modifier = Modifier.weight(1f),
          onClick = { viewModel.setTheme(true) }
        )
        VCButton(
          content = VCButtonContent.Text(R.string.settings_theme_system),
          style = if (uiState.isDarkTheme == null) VCButtonStyle.Primary else VCButtonStyle.Outline,
          modifier = Modifier.weight(1f),
          onClick = { viewModel.setTheme(null) }
        )
        VCButton(
          content = VCButtonContent.Text(R.string.settings_theme_light),
          style = if (uiState.isDarkTheme == false) VCButtonStyle.Primary else VCButtonStyle.Outline,
          modifier = Modifier.weight(1f),
          onClick = { viewModel.setTheme(false) }
        )
      }

      Spacer(Modifier.height(4.dp))

      SectionLabel(stringResource(R.string.settings_contrast))

      val contrastLabels = listOf(
        stringResource(R.string.settings_contrast_low),
        stringResource(R.string.settings_contrast_medium),
        stringResource(R.string.settings_contrast_high)
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        contrastLabels.forEachIndexed { index, label ->
          VCButton(
            content = VCButtonContent.Text(
              when (index) {
                0 -> R.string.settings_contrast_low
                1 -> R.string.settings_contrast_medium
                else -> R.string.settings_contrast_high
              }
            ),
            style = if (uiState.contrastType == index) VCButtonStyle.Primary else VCButtonStyle.Outline,
            modifier = Modifier.weight(1f),
            onClick = { viewModel.setContrast(index) }
          )
        }
      }

      Spacer(Modifier.height(4.dp))

      SectionLabel(stringResource(R.string.settings_font_size, String.format("%.1f", uiState.fontSizeMultiplier)))

      Slider(
        value = uiState.fontSizeMultiplier.toFloat(),
        onValueChange = { raw ->
          val step = 0.1f
          viewModel.setFontSize(((raw / step).roundToInt() * step).toDouble())
        },
        valueRange = 0.75f..2f,
        steps = 12,
        modifier = Modifier.fillMaxWidth()
      )

      HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

      // ----- Logout -----
      VCButton(
        content = VCButtonContent.Text(R.string.settings_logout),
        style = VCButtonStyle.Outline,
        modifier = Modifier.fillMaxWidth(),
        onClick = { viewModel.logout() }
      )

      // ----- Desarrollo (solo debug) -----
      if (BuildConfig.DEBUG) {
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        DebugServerSection(
          currentUrl = uiState.debugServerUrl,
          onSave = { viewModel.saveDebugServerUrl(it) }
        )
      }

      Spacer(Modifier.height(16.dp))
    }
  }
}

@Composable
private fun DebugServerSection(currentUrl: String, onSave: (String) -> Unit) {
  var localUrl by remember(currentUrl) { mutableStateOf(currentUrl) }

  SectionTitle("Servidor de desarrollo")
  SectionLabel("Activo: ${currentUrl.ifBlank { BuildConfig.BASE_URL }}")
  Spacer(Modifier.height(4.dp))

  OutlinedTextField(
    value = localUrl,
    onValueChange = { localUrl = it },
    label = { Text("URL del servidor") },
    placeholder = { Text("http://192.168.1.100:3000/") },
    singleLine = true,
    modifier = Modifier.fillMaxWidth()
  )

  Text(
    text = "Vacío → usa la URL del flavor (${BuildConfig.BASE_URL})",
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )

  Spacer(Modifier.height(8.dp))

  OutlinedButton(
    onClick = { onSave(localUrl) },
    modifier = Modifier.fillMaxWidth()
  ) {
    Text("Guardar y reiniciar")
  }
}

@Composable
private fun SectionTitle(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold
  )
}

@Composable
private fun SectionLabel(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.labelLarge,
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )
}
