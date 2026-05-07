/**
 * Social login buttons for the auth screen.
 *
 * [SocialButton] — full-width outlined button with the provider icon pinned to the left and
 * the label centered. Used on medium+ screens.
 * [SocialIconButton] — compact icon-only variant for small screens (shown in a Row of three).
 *
 * Both buttons use surfaceContainerLow + outlineVariant so they adapt to light/dark theme without
 * relying on provider brand colors as background. Icon tinting strategy per provider:
 *   - Google  → no tint (multicolor logo)
 *   - Apple   → tint with onSurface (black in light, white in dark)
 *   - Facebook → tint with [FacebookBlue] (#1877F2, always visible on any surface)
 */
package es.virtualclubs.presentation.screens.auth

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.virtualclubs.presentation.theme.VCTheme

internal val FacebookBlue: Color = Color(0xFF1877F2)

@Composable
internal fun SocialButton(
  @StringRes text: Int,
  @DrawableRes icon: Int,
  iconTint: Color?,
  onClick: () -> Unit
) {
  val onSurface = MaterialTheme.colorScheme.onSurface
  OutlinedButton(
    onClick = onClick,
    modifier = Modifier.fillMaxWidth().height(48.dp),
    shape = VCTheme.shapes.medium,
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
      contentColor = onSurface
    ),
    contentPadding = PaddingValues(horizontal = VCTheme.spacing.screenHorizontal)
  ) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
      Image(
        painter = painterResource(icon),
        contentDescription = null,
        colorFilter = remember(iconTint) { iconTint?.let { ColorFilter.tint(it) } },
        modifier = Modifier.align(Alignment.CenterStart).size(20.dp)
      )
      Text(stringResource(text))
    }
  }
}

@Composable
internal fun SocialIconButton(
  @DrawableRes icon: Int,
  @StringRes contentDesc: Int,
  iconTint: Color?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  OutlinedButton(
    onClick = onClick,
    modifier = modifier.height(48.dp),
    shape = VCTheme.shapes.medium,
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
      contentColor = MaterialTheme.colorScheme.onSurface
    ),
    contentPadding = PaddingValues(0.dp)
  ) {
    Image(
      painter = painterResource(icon),
      contentDescription = stringResource(contentDesc),
      colorFilter = remember(iconTint) { iconTint?.let { ColorFilter.tint(it) } },
      modifier = Modifier.size(24.dp)
    )
  }
}
