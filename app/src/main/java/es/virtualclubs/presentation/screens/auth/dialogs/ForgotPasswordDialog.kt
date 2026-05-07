/**
 * Dialog for the "Forgot password" flow.
 * Collects the user's email and delegates to [AuthViewModel.requestPasswordReset].
 * Dismisses automatically on [PasswordResetUiState.Success].
 */
package es.virtualclubs.presentation.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.virtualclubs.R
import es.virtualclubs.presentation.managers.LocalGlobalUIManager
import es.virtualclubs.presentation.components.RoundedTextField
import es.virtualclubs.presentation.components.VCButton
import es.virtualclubs.presentation.components.VCButtonContent
import es.virtualclubs.presentation.components.VCButtonStyle
import es.virtualclubs.presentation.theme.VCTheme

@Preview
@Composable
private fun ForgotPasswordDialogPreview() {
  MaterialTheme {
    ForgotPasswordDialog(
      passwordResetUiState = PasswordResetUiState.Idle,
      onDismiss = {},
      onConfirm = {}
    )
  }
}

@Composable
internal fun ForgotPasswordDialog(
  passwordResetUiState: PasswordResetUiState,
  onDismiss: () -> Unit,
  onConfirm: (String) -> Unit
) {
  var email by remember { mutableStateOf("") }
  val globalUIManager = LocalGlobalUIManager.current

  if (passwordResetUiState is PasswordResetUiState.Success) onDismiss()

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(stringResource(R.string.forgot_password)) },
    text = {
      Column {
        Text(
          text = stringResource(R.string.forgot_password_description),
          style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        RoundedTextField(
          value = email,
          onValueChange = { email = it },
          placeholder = R.string.email_placeholder,
          leadingIcon = Icons.Default.Email,
          keyboardType = KeyboardType.Email,
          modifier = Modifier.fillMaxWidth()
        )
        if (globalUIManager.haveError()) {
          Text(
            text = stringResource(globalUIManager.getErrorId()),
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(vertical = 8.dp)
          )
        }
      }
    },
    confirmButton = {
      VCButton(
        content = VCButtonContent.Text(R.string.send_email),
        enabled = email.isNotBlank() && passwordResetUiState !is PasswordResetUiState.Attempting,
        shape = VCTheme.shapes.large,
        onClick = { onConfirm(email) }
      )
    },
    dismissButton = {
      VCButton(
        content = VCButtonContent.Text(R.string.close),
        style = VCButtonStyle.Text,
        onClick = onDismiss
      )
    }
  )
}
