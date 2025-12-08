package es.virtualclubs.presentation.components.dialogs

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.stringResource
import es.virtualclubs.R
import es.virtualclubs.data.managers.GlobalUIManager
import es.virtualclubs.data.managers.GlobalUIManager.showDialog
import es.virtualclubs.data.managers.SafeCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun showEmailNotVerifiedDialog() {
  showDialog(
    title = R.string.email_not_verified,
    content = {
      Text(stringResource(R.string.email_not_verified_message))
      TextButton(onClick = {
        val authRepository = GlobalUIManager.getEntryPoint().authRepository()
        CoroutineScope(Dispatchers.IO).launch {
          SafeCall.safeCall { authRepository.requestVerify() }
        }
      }) {
        Text(stringResource(R.string.send_verification_email))
      }
    },
    blockDialog = true,
    dismissible = false,
  )
}