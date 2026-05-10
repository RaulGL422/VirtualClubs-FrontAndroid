package es.virtualclubs.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import es.virtualclubs.R
import es.virtualclubs.presentation.theme.VCPreviewTheme
import es.virtualclubs.presentation.theme.VCTheme
import kotlin.math.roundToInt

private val contrastOptions = listOf(
    R.string.settings_contrast_low    to 0,
    R.string.settings_contrast_medium to 1,
    R.string.settings_contrast_high   to 2
)

@Composable
internal fun AppearanceSection(
    uiState: SettingsUiState,
    onThemeChange: (Boolean?) -> Unit,
    onContrastChange: (Int) -> Unit,
    onFontSizeChange: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = VCTheme.spacing
    val segmentedColors = SegmentedButtonDefaults.colors(
        activeContainerColor = VCTheme.colors.primaryContainer,
        activeContentColor   = VCTheme.colors.onPrimaryContainer,
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text  = stringResource(R.string.settings_theme),
            style = VCTheme.typography.labelLarge,
            color = VCTheme.colors.onSurfaceVariant
        )
        Spacer(Modifier.height(spacing.sm))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = uiState.isDarkTheme == false,
                onClick  = { onThemeChange(false) },
                shape    = SegmentedButtonDefaults.itemShape(index = 0, count = 3, baseShape = MaterialTheme.shapes.small),
                colors   = segmentedColors
            ) { Text(stringResource(R.string.settings_theme_light)) }

            SegmentedButton(
                selected = uiState.isDarkTheme == null,
                onClick  = { onThemeChange(null) },
                shape    = SegmentedButtonDefaults.itemShape(index = 1, count = 3, baseShape = MaterialTheme.shapes.small),
                colors   = segmentedColors
            ) { Text(stringResource(R.string.settings_theme_system)) }

            SegmentedButton(
                selected = uiState.isDarkTheme == true,
                onClick  = { onThemeChange(true) },
                shape    = SegmentedButtonDefaults.itemShape(index = 2, count = 3, baseShape = MaterialTheme.shapes.small),
                colors   = segmentedColors
            ) { Text(stringResource(R.string.settings_theme_dark)) }
        }

        Spacer(Modifier.height(spacing.xl))

        Text(
            text  = stringResource(R.string.settings_contrast),
            style = VCTheme.typography.labelLarge,
            color = VCTheme.colors.onSurfaceVariant
        )
        Spacer(Modifier.height(spacing.sm))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            contrastOptions.forEachIndexed { index, (labelRes, value) ->
                SegmentedButton(
                    selected = uiState.contrastType == value,
                    onClick  = { onContrastChange(value) },
                    shape    = SegmentedButtonDefaults.itemShape(index = index, count = 3, baseShape = MaterialTheme.shapes.small),
                    colors   = segmentedColors
                ) { Text(stringResource(labelRes)) }
            }
        }

        Spacer(Modifier.height(spacing.xl))

        val fontStep = (uiState.fontSizeMultiplier * 10).roundToInt().coerceIn(8, 20)
        Row(
            modifier              = Modifier.fillMaxWidth(),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text  = stringResource(R.string.settings_font_size),
                style = VCTheme.typography.labelLarge,
                color = VCTheme.colors.onSurfaceVariant
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                FilledTonalIconButton(
                    onClick  = { onFontSizeChange((fontStep - 1) * 0.1) },
                    enabled  = fontStep > 8,
                    shape    = VCTheme.shapes.medium,
                    colors   = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = VCTheme.colors.surfaceContainerHigh,
                        contentColor   = VCTheme.colors.onSurface
                    )
                ) { Icon(Icons.Filled.Remove, contentDescription = null) }

                Text(
                    text      = "${fontStep * 10}%",
                    style     = VCTheme.typography.titleSmall,
                    color     = VCTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.padding(horizontal = spacing.md)
                )

                FilledTonalIconButton(
                    onClick  = { onFontSizeChange((fontStep + 1) * 0.1) },
                    enabled  = fontStep < 20,
                    shape    = VCTheme.shapes.medium,
                    colors   = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = VCTheme.colors.surfaceContainerHigh,
                        contentColor   = VCTheme.colors.onSurface
                    )
                ) { Icon(Icons.Filled.Add, contentDescription = null) }
            }
        }

        Spacer(Modifier.height(spacing.sm))

        Surface(
            shape    = VCTheme.shapes.medium,
            color    = VCTheme.colors.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text      = stringResource(R.string.settings_font_preview),
                style     = VCTheme.typography.bodyMedium,
                color     = VCTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier  = Modifier.padding(spacing.componentPaddingMd)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppearanceSectionPreview() {
    VCPreviewTheme {
        Surface {
            AppearanceSection(
                uiState          = SettingsUiState(isDarkTheme = null, contrastType = 0, fontSizeMultiplier = 1.0),
                onThemeChange    = {},
                onContrastChange = {},
                onFontSizeChange = {}
            )
        }
    }
}
