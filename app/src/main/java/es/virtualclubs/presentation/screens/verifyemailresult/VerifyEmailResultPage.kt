package es.virtualclubs.presentation.screens.verifyemailresult

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import es.virtualclubs.R
import es.virtualclubs.ScreenType
import es.virtualclubs.presentation.components.VCButton
import es.virtualclubs.presentation.components.VCButtonContent
import es.virtualclubs.presentation.components.VCButtonStyle
import es.virtualclubs.presentation.components.VCIcon
import es.virtualclubs.presentation.components.VCScaffold
import es.virtualclubs.presentation.theme.VCPreviewTheme
import es.virtualclubs.presentation.theme.VCTheme

@Composable
fun VerifyEmailResultPage(
  viewModel: VerifyEmailResultViewModel = hiltViewModel(),
  onGoToHome: () -> Unit,
  onSettingsTap: () -> Unit,
  screenType: ScreenType
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  VCScaffold(enableTopBar = false) { padding ->
    val columnWidthFraction = when (screenType) {
      ScreenType.Small -> 0.85f
      ScreenType.Medium -> 0.45f
    }

    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth(columnWidthFraction)
          .align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        when (uiState) {
          VerifyEmailResultUiState.Success -> VerifyEmailResultContent(
            icon = Icons.Filled.CheckCircle,
            iconTint = VCTheme.colors.primary,
            title = R.string.email_verified_success_title,
            message = R.string.email_verified_success_message,
            buttonText = R.string.go_to_home,
            onButtonClick = onGoToHome
          )
          VerifyEmailResultUiState.Error -> VerifyEmailResultContent(
            icon = Icons.Filled.Cancel,
            iconTint = VCTheme.colors.error,
            title = R.string.email_verified_error_title,
            message = R.string.email_verified_error_message,
            buttonText = R.string.go_to_home,
            onButtonClick = onGoToHome
          )
        }
      }

      VCButton(
        content = VCButtonContent.Icon(VCIcon.Vector(Icons.Filled.Settings)),
        style = VCButtonStyle.Icon,
        iconSize = VCTheme.sizes.iconMd,
        onClick = onSettingsTap,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(
            top = VCTheme.spacing.screenPaddingDouble,
            end = VCTheme.spacing.screenHorizontal
          )
      )
    }
  }
}

@Composable
private fun VerifyEmailResultContent(
  icon: ImageVector,
  iconTint: Color,
  @StringRes title: Int,
  @StringRes message: Int,
  @StringRes buttonText: Int,
  onButtonClick: () -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = iconTint,
      modifier = Modifier.size(80.dp)
    )
    Text(
      text = stringResource(title),
      style = VCTheme.typography.headlineSmall,
      textAlign = TextAlign.Center
    )
    Text(
      text = stringResource(message),
      style = VCTheme.typography.bodyMedium,
      textAlign = TextAlign.Center,
      color = VCTheme.colors.onSurfaceVariant
    )
    VCButton(
      content = VCButtonContent.Text(buttonText),
      shape = VCTheme.shapes.small,
      onClick = onButtonClick,
      modifier = Modifier
        .fillMaxWidth()
        .height(VCTheme.sizes.buttonHeightMd)
    )
  }
}

@Preview(showBackground = true, name = "Éxito")
@Composable
private fun VerifyEmailResultSuccessPreview() {
  VCPreviewTheme {
    VerifyEmailResultContent(
      icon = Icons.Filled.CheckCircle,
      iconTint = VCTheme.colors.primary,
      title = R.string.email_verified_success_title,
      message = R.string.email_verified_success_message,
      buttonText = R.string.go_to_home,
      onButtonClick = {}
    )
  }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun VerifyEmailResultErrorPreview() {
  VCPreviewTheme {
    VerifyEmailResultContent(
      icon = Icons.Filled.Cancel,
      iconTint = VCTheme.colors.error,
      title = R.string.email_verified_error_title,
      message = R.string.email_verified_error_message,
      buttonText = R.string.go_to_home,
      onButtonClick = {}
    )
  }
}
