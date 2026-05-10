package es.virtualclubs.presentation.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import es.virtualclubs.presentation.components.VCListToggleItem

@Composable
internal fun SettingToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) = VCListToggleItem(title, subtitle, icon, checked, onToggle, modifier)
