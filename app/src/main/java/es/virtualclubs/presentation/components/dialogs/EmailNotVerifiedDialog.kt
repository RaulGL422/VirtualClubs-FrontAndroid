package es.virtualclubs.presentation.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import es.virtualclubs.R
import es.virtualclubs.data.managers.GlobalUIManager
import es.virtualclubs.data.managers.GlobalUIManager.showDialog
import es.virtualclubs.presentation.components.VCButton
import es.virtualclubs.presentation.components.VCButtonContent
import es.virtualclubs.presentation.theme.VCTheme

fun showEmailNotVerifiedDialog() {
  showDialog(
    title = R.string.email_not_verified,
    content = {
      Text(stringResource(R.string.email_not_verified_message))
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = VCTheme.spacing.sectionSpacingDouble)
      ) {
        VCButton(
          content = VCButtonContent.Text(R.string.send_verification_email),
          height = VCTheme.sizes.buttonHeightMd
        ) {
          GlobalUIManager.requestVerifyEmail()
        }
      }
    },
    blockDialog = true,
    dismissible = false,
  )
}