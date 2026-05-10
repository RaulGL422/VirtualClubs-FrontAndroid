package es.virtualclubs.presentation.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import es.virtualclubs.presentation.components.VCListItem

@Composable
internal fun SettingRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) = VCListItem(title, subtitle, icon, onClick, modifier)
