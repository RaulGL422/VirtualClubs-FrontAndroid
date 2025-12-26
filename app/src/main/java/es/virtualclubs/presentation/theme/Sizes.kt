package es.virtualclubs.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Sizes(
  // Icons
  val iconXs: Dp = 16.dp,
  val iconSm: Dp = 20.dp,
  val iconMd: Dp = 24.dp,
  val iconLg: Dp = 32.dp,

  // Buttons
  val buttonHeightSm: Dp = 40.dp,
  val buttonHeightMd: Dp = 48.dp,
  val buttonHeightLg: Dp = 56.dp,

  // Avatars / Images
  val avatarSm: Dp = 32.dp,
  val avatarMd: Dp = 40.dp,
  val avatarLg: Dp = 56.dp,
  val avatarXl: Dp = 72.dp,

  // Cards
  val cardMinHeight: Dp = 120.dp,
  val cardImageHeight: Dp = 180.dp,

  // Lists
  val listItemMinHeight: Dp = 56.dp,

  // Sheets / Dialogs
  val bottomSheetPeek: Dp = 64.dp,
  val dialogMinWidth: Dp = 280.dp,

  // Inputs
  val inputHeight: Dp = 56.dp,
)