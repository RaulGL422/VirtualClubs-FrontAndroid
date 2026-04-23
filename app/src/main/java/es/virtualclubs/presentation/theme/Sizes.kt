package es.virtualclubs.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Sizes(

    // Icons
    val iconXs: Dp = 16.dp,
    val iconSm: Dp = 20.dp,
    val iconMd: Dp = 24.dp,
    val iconLg: Dp = 32.dp,
    val iconXl: Dp = 48.dp,

    // Buttons
    val buttonHeightSm: Dp = 40.dp,
    val buttonHeightMd: Dp = 52.dp,
    val buttonHeightLg: Dp = 60.dp,

    // Avatars / Images
    val avatarSm: Dp = 32.dp,
    val avatarMd: Dp = 48.dp,
    val avatarLg: Dp = 64.dp,
    val avatarXl: Dp = 80.dp,

    // Cards
    val cardMinHeight   : Dp = 120.dp,
    val cardImageHeight : Dp = 180.dp,
    val cardBannerHeight: Dp = 220.dp,

    // Chips / Badges
    val chipHeight: Dp = 32.dp,
    val badgeSize : Dp = 20.dp,

    // Lists
    val listItemMinHeight: Dp = 56.dp,

    // Sheets / Dialogs
    val bottomSheetPeek: Dp = 64.dp,
    val dialogMinWidth : Dp = 280.dp,

    // Inputs
    val inputHeight: Dp = 56.dp,
)
