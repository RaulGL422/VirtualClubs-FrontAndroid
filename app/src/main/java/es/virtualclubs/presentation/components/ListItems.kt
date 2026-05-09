package es.virtualclubs.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import es.virtualclubs.presentation.theme.VCTheme

/**
 * Fila de contenido con icono, título, subtítulo y flecha opcional.
 * Uso: ajustes, perfiles, listas de opciones navegables.
 *
 * @param onClick `null` desactiva la interacción y oculta la flecha.
 */
@Composable
fun VCListItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val spacing = VCTheme.spacing

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(VCTheme.shapes.medium)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = spacing.itemVerticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.inlineSpacingMd)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (onClick != null) VCTheme.colors.primary else VCTheme.colors.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = VCTheme.typography.bodyMedium,
                color = VCTheme.colors.onSurface
            )
            Text(
                text = subtitle,
                style = VCTheme.typography.bodySmall,
                color = VCTheme.colors.onSurfaceVariant
            )
        }
        if (onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = VCTheme.colors.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Variante de [VCListItem] con `Switch` en lugar de flecha.
 * Uso: ajustes tipo on/off (notificaciones, modos, flags de funcionalidad).
 */
@Composable
fun VCListToggleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = VCTheme.spacing

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.itemVerticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.inlineSpacingMd)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = VCTheme.colors.primary,
            modifier = Modifier.size(24.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = VCTheme.typography.bodyMedium,
                color = VCTheme.colors.onSurface
            )
            Text(
                text = subtitle,
                style = VCTheme.typography.bodySmall,
                color = VCTheme.colors.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle
        )
    }
}
