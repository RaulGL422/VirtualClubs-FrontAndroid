package es.virtualclubs.presentation.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import es.virtualclubs.R
import es.virtualclubs.domain.dialogs.VCDialog
import es.virtualclubs.presentation.components.VCButton
import es.virtualclubs.presentation.components.VCButtonContent
import es.virtualclubs.presentation.managers.GlobalUIManager
import es.virtualclubs.presentation.theme.VCTheme

/**
 * Diálogo que se muestra cuando el usuario intenta realizar una acción
 * con el email sin verificar. Permite reenviar el correo de verificación.
 */
object EmailNotVerifiedDialog : VCDialog {
    override val titleRes = R.string.email_not_verified
    override val dismissible = false
    override val blockDialog = true

    @Composable
    override fun ColumnScope.Content() {
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
    }
}
