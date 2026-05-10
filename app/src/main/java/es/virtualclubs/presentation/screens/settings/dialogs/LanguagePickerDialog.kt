package es.virtualclubs.presentation.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import es.virtualclubs.R
import es.virtualclubs.presentation.components.dialogs.VCDialog
import es.virtualclubs.presentation.theme.VCTheme

internal class LanguagePickerDialog(
    currentLanguage: String,
    private val onLanguageSelected: @Composable (String) -> Unit
) : VCDialog {

    private val selected = mutableStateOf(currentLanguage)

    override val titleRes      = R.string.settings_language
    override val dismissible   = true
    override val confirmTextRes = R.string.confirm
    override val dismissTextRes = R.string.cancel

    override val onConfirm: (() -> Unit) = {
        onLanguageSelected(selected.value)
    }

    @Composable
    override fun ColumnScope.Content() {
        val spacing = VCTheme.spacing
        val options = listOf(
            "" to stringResource(R.string.settings_language_system),
            "es" to "Español",
            "en" to "English"
        )

        Column(modifier = Modifier.selectableGroup()) {
            options.forEach { (tag, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selected.value == tag,
                            onClick  = { selected.value = tag },
                            role     = Role.RadioButton
                        )
                        .padding(vertical = spacing.itemVerticalPaddingCompact),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected.value == tag,
                        onClick  = null
                    )
                    Text(
                        text     = label,
                        style    = VCTheme.typography.bodyMedium,
                        color    = VCTheme.colors.onSurface,
                        modifier = Modifier.padding(start = spacing.inlineSpacingSm)
                    )
                }
            }
        }
    }
}
