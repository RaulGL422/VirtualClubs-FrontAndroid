/**
 * Horizontal "O" divider shown between the primary action button and the social login buttons.
 */
package es.virtualclubs.presentation.screens.auth

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun AuthDivider() {
  Row(verticalAlignment = Alignment.CenterVertically) {
    HorizontalDivider(Modifier.weight(1f))
    Text("  O  ", color = Color.Gray)
    HorizontalDivider(Modifier.weight(1f))
  }
}
