package es.virtualclubs.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.virtualclubs.presentation.theme.Spacing
import es.virtualclubs.presentation.theme.VCTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

enum class VCButtonStyle {
  Primary,
  Secondary,
  Outline,
  Text,
  Icon
}

sealed class VCIcon {
  data class Vector(
    val imageVector: ImageVector,
    val contentDescription: String? = null
  ) : VCIcon()

  data class Drawable(
    @param:DrawableRes val resId: Int,
    val contentDescription: String? = null
  ) : VCIcon()
}

@Composable
fun VCIconRender(
  icon: VCIcon,
  modifier: Modifier = Modifier,
) {
  when (icon) {
    is VCIcon.Vector -> {
      Icon(
        imageVector = icon.imageVector,
        contentDescription = icon.contentDescription,
        modifier = modifier,
      )
    }

    is VCIcon.Drawable -> {
      Icon(
        painter = painterResource(icon.resId),
        contentDescription = icon.contentDescription,
        modifier = modifier,
      )
    }
  }
}

@Composable
fun vcButtonColors(
  style: VCButtonStyle,
  containerColor: Color? = null,
  contentColor: Color? = null,
): ButtonColors =
  when (style) {
    VCButtonStyle.Primary ->
      ButtonDefaults.buttonColors(
        containerColor = (containerColor ?: VCTheme.colors.primary),
        contentColor = contentColor ?: VCTheme.colors.onPrimary
      )

    VCButtonStyle.Secondary ->
      ButtonDefaults.buttonColors(
        containerColor = containerColor ?: VCTheme.colors.secondary,
        contentColor = contentColor ?: VCTheme.colors.onSecondary
      )

    VCButtonStyle.Outline ->
      ButtonDefaults.outlinedButtonColors(
        contentColor = contentColor ?: VCTheme.colors.primary
      )

    VCButtonStyle.Text ->
      ButtonDefaults.textButtonColors(
        contentColor = contentColor ?: VCTheme.colors.primary
      )

    VCButtonStyle.Icon ->
      ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = contentColor ?: VCTheme.colors.primary
      )
  }

sealed class VCButtonContent {
  data class Text(
    @param:StringRes val text: Int
  ) : VCButtonContent()

  data class Icon(
    val icon: VCIcon
  ) : VCButtonContent()

  data class TextAndIcon(
    @param:StringRes val text: Int,
    val icon: VCIcon
  ) : VCButtonContent()
}

@Composable
fun VCButton(
  content: VCButtonContent,
  modifier: Modifier = Modifier,
  style: VCButtonStyle = VCButtonStyle.Primary,
  enabled: Boolean = true,
  height: Dp = 48.dp,
  iconSize: Dp = 20.dp,
  shape: RoundedCornerShape = VCTheme.shapes.medium,
  colors: ButtonColors = vcButtonColors(style),
  onClick: () -> Unit
) {
  val spacing = VCTheme.spacing

  when (style) {
    VCButtonStyle.Icon -> {
      val iconContent = content as? VCButtonContent.Icon
        ?: error("VCButtonStyle.Icon requires VCButtonContent.Icon")

      IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
      ) {
        VCIconRender(
          icon = iconContent.icon,
          modifier = Modifier.size(iconSize)
        )
      }
    }

    VCButtonStyle.Outline -> {
      OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(height),
        shape = shape,
        colors = colors
      ) {
        ButtonContent(content, spacing, iconSize)
      }
    }

    VCButtonStyle.Text -> {
      TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        colors = colors
      ) {
        ButtonContent(content, spacing, iconSize)
      }
    }

    else -> {
      Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(height),
        shape = shape,
        colors = colors
      ) {
        ButtonContent(content, spacing, iconSize)
      }
    }
  }
}

@Composable
private fun ButtonContent(
  content: VCButtonContent,
  spacing: Spacing,
  iconSize: Dp
) {
  when (content) {
    is VCButtonContent.Text -> {
      Text(stringResource(content.text))
    }

    is VCButtonContent.Icon -> {
      VCIconRender(
        icon = content.icon,
        modifier = Modifier.size(iconSize)
      )
    }

    is VCButtonContent.TextAndIcon -> {
      VCIconRender(
        icon = content.icon,
        modifier = Modifier.size(iconSize)
      )

      Spacer(Modifier.width(spacing.buttonSpacing))

      Text(stringResource(content.text))
    }
  }
}

@Composable
fun VCDropdownButton(
  @StringRes text: Int,
  options: List<String>,
  onSelected: (String) -> Unit,
  modifier: Modifier = Modifier,
  style: VCButtonStyle = VCButtonStyle.Primary
) {
  var expanded by remember { mutableStateOf(false) }

  Box(modifier) {
    VCButton(
      content = VCButtonContent.TextAndIcon(
        text,
        VCIcon.Vector(if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown)
      ),
      style = style,
      onClick = { expanded = true }
    )

    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false }
    ) {
      options.forEach { option ->
        DropdownMenuItem(
          text = { Text(option) },
          onClick = {
            expanded = false
            onSelected(option)
          }
        )
      }
    }
  }
}