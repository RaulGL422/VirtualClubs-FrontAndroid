package es.virtualclubs.presentation.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import es.virtualclubs.BuildConfig
import es.virtualclubs.R
import es.virtualclubs.presentation.theme.VCTheme

@Composable
internal fun DebugServerSection(currentUrl: String, onSave: (String) -> Unit) {
    var localUrl by remember(currentUrl) { mutableStateOf(currentUrl) }
    val spacing = VCTheme.spacing

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text  = stringResource(R.string.settings_debug_server_title),
            style = VCTheme.typography.titleMedium,
            color = VCTheme.colors.onSurface
        )
        Text(
            text  = stringResource(R.string.settings_debug_server_active, currentUrl.ifBlank { BuildConfig.BASE_URL }),
            style = VCTheme.typography.labelLarge,
            color = VCTheme.colors.onSurfaceVariant
        )
        Spacer(Modifier.height(spacing.sm))

        OutlinedTextField(
            value         = localUrl,
            onValueChange = { localUrl = it },
            label         = { Text(stringResource(R.string.settings_debug_server_url_label)) },
            placeholder   = { Text("http://192.168.1.100:3000/") },
            singleLine    = true,
            modifier      = Modifier.fillMaxWidth()
        )

        Text(
            text  = stringResource(R.string.settings_debug_server_hint, BuildConfig.BASE_URL),
            style = VCTheme.typography.bodySmall,
            color = VCTheme.colors.onSurfaceVariant
        )

        Spacer(Modifier.height(spacing.md))

        OutlinedButton(
            onClick  = { onSave(localUrl) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.settings_debug_server_save))
        }
    }
}
