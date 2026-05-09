package es.virtualclubs.presentation.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import es.virtualclubs.R
import es.virtualclubs.presentation.theme.VCTheme
import kotlin.math.roundToInt

@Composable
internal fun AppearanceSection(
    uiState: SettingsUiState,
    onThemeChange: (Boolean?) -> Unit,
    onContrastChange: (Int) -> Unit,
    onFontSizeChange: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = VCTheme.spacing

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.settings_theme),
            style = VCTheme.typography.labelLarge,
            color = VCTheme.colors.onSurfaceVariant
        )
        Spacer(Modifier.height(spacing.sm))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = uiState.isDarkTheme == false,
                onClick = { onThemeChange(false) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
            ) { Text(stringResource(R.string.settings_theme_light)) }

            SegmentedButton(
                selected = uiState.isDarkTheme == null,
                onClick = { onThemeChange(null) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
            ) { Text(stringResource(R.string.settings_theme_system)) }

            SegmentedButton(
                selected = uiState.isDarkTheme == true,
                onClick = { onThemeChange(true) },
                shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
            ) { Text(stringResource(R.string.settings_theme_dark)) }
        }

        Spacer(Modifier.height(spacing.xl))

        Text(
            text = stringResource(R.string.settings_contrast),
            style = VCTheme.typography.labelLarge,
            color = VCTheme.colors.onSurfaceVariant
        )
        Spacer(Modifier.height(spacing.sm))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            listOf(
                R.string.settings_contrast_low   to 0,
                R.string.settings_contrast_medium to 1,
                R.string.settings_contrast_high   to 2
            ).forEachIndexed { index, (labelRes, value) ->
                SegmentedButton(
                    selected = uiState.contrastType == value,
                    onClick = { onContrastChange(value) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 3)
                ) { Text(stringResource(labelRes)) }
            }
        }

        Spacer(Modifier.height(spacing.xl))

        Text(
            text = stringResource(
                R.string.settings_font_size,
                String.format("%.1f", uiState.fontSizeMultiplier)
            ),
            style = VCTheme.typography.labelLarge,
            color = VCTheme.colors.onSurfaceVariant
        )
        Spacer(Modifier.height(spacing.sm))

        Slider(
            value = uiState.fontSizeMultiplier.toFloat(),
            onValueChange = { raw ->
                val step = 0.1f
                onFontSizeChange(((raw / step).roundToInt() * step).toDouble())
            },
            valueRange = 0.75f..2f,
            steps = 12,
            modifier = Modifier.fillMaxWidth()
        )

        Surface(
            shape = VCTheme.shapes.medium,
            color = VCTheme.colors.surfaceContainerLow,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings_font_preview),
                style = VCTheme.typography.bodyMedium,
                color = VCTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(spacing.componentPaddingMd)
            )
        }
    }
}
