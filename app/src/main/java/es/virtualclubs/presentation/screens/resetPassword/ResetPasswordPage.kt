package es.virtualclubs.presentation.screens.resetPassword

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import es.virtualclubs.R
import es.virtualclubs.ScreenType
import es.virtualclubs.presentation.managers.LocalGlobalUIManager
import es.virtualclubs.presentation.components.PasswordRequirements
import es.virtualclubs.presentation.components.RoundedTextField
import es.virtualclubs.presentation.components.meetsAllPasswordRequirements
import es.virtualclubs.presentation.components.VCButton
import es.virtualclubs.presentation.components.VCButtonContent
import es.virtualclubs.presentation.components.VCButtonStyle
import es.virtualclubs.presentation.components.VCIcon
import es.virtualclubs.presentation.components.VCScaffold
import es.virtualclubs.presentation.theme.VCTheme

@Composable
fun ResetPasswordPage(
  @StringRes titlePage: Int,
  token: String,
  screenType: ScreenType,
  viewModel: ResetPasswordViewModel = hiltViewModel(),
  onSettingsTap: () -> Unit,
  onPasswordResetSuccess: (String) -> Unit
) {
  val uiState by viewModel.uiState.collectAsState()
  var newPassword by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }

  val newPasswordFocusRequester = remember { FocusRequester() }
  val confirmPasswordFocusRequester = remember { FocusRequester() }
  val focusManager = LocalFocusManager.current
  val globalUIManager = LocalGlobalUIManager.current

  if (uiState is ResetPasswordUiState.Success) onPasswordResetSuccess(stringResource(R.string.password_reset_success))

  VCScaffold(
    titleTopBar = titlePage,
    topBarActions = {
      VCButton(
        content = VCButtonContent.Icon(VCIcon.Vector(Icons.Filled.Settings)),
        style = VCButtonStyle.Icon,
        iconSize = VCTheme.sizes.iconMd,
        onClick = onSettingsTap
      )
    },
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      contentAlignment = Alignment.Center
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth(
            when (screenType) {
              ScreenType.Medium -> 0.45f
              ScreenType.Small -> 0.85f
            }
          )
          .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        ResetPasswordIcon()

        Spacer(modifier = Modifier.height(VCTheme.spacing.xxxl))

        Text(
          text = stringResource(R.string.introduce_reset_password),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(VCTheme.spacing.xxl))

        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          ResetPasswordField(
            value = newPassword,
            onValueChange = { newPassword = it },
            placeholder = R.string.password_placeholder,
            imeAction = ImeAction.Next,
            onImeAction = { confirmPasswordFocusRequester.requestFocus() },
            modifier = Modifier
              .fillMaxWidth()
              .focusRequester(newPasswordFocusRequester)
          )

          Spacer(modifier = Modifier.height(VCTheme.spacing.md))

          PasswordRequirements(
            password = newPassword,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(VCTheme.spacing.lg))

          ResetPasswordField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = R.string.confirm_password_placeholder,
            onImeAction = {
              focusManager.clearFocus()
              viewModel.resetPassword(token, newPassword, confirmPassword)
            },
            modifier = Modifier
              .fillMaxWidth()
              .focusRequester(confirmPasswordFocusRequester)
          )
        }

        Spacer(modifier = Modifier.height(VCTheme.spacing.xl))

        AnimatedVisibility(visible = globalUIManager.haveError()) {
          Text(
            text = stringResource(globalUIManager.getErrorId()),
            color = VCTheme.colors.error,
            modifier = Modifier.padding(bottom = VCTheme.spacing.md)
          )
        }

        VCButton(
          content = VCButtonContent.Text(R.string.change_password),
          enabled = meetsAllPasswordRequirements(newPassword) &&
              confirmPassword.isNotBlank() &&
              uiState !is ResetPasswordUiState.Attempting,
          shape = VCTheme.shapes.small,
          onClick = {
            viewModel.resetPassword(token, newPassword, confirmPassword)
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(VCTheme.sizes.buttonHeightMd)
        )
      }
    }
  }
}

@Composable
private fun ResetPasswordIcon() {
  Box(
    modifier = Modifier
      .size(VCTheme.sizes.avatarXl)
      .background(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = CircleShape
      ),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = Icons.Filled.Lock,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onPrimaryContainer,
      modifier = Modifier.size(VCTheme.sizes.iconXl)
    )
  }
}

@Composable
private fun ResetPasswordField(
  value: String,
  onValueChange: (String) -> Unit,
  @StringRes placeholder: Int,
  modifier: Modifier = Modifier,
  imeAction: ImeAction = ImeAction.Done,
  onImeAction: () -> Unit = {}
) {
  RoundedTextField(
    value = value,
    onValueChange = onValueChange,
    leadingIcon = Icons.Default.Lock,
    placeholder = placeholder,
    isPassword = true,
    keyboardType = KeyboardType.Password,
    imeAction = imeAction,
    onImeAction = onImeAction,
    modifier = modifier
  )
}
