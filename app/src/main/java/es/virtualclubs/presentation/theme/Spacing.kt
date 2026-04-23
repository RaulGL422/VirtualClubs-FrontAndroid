package es.virtualclubs.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(

    /* ───────────── Base scale ───────────── */

    val xs  : Dp = 2.dp,
    val sm  : Dp = 4.dp,
    val md  : Dp = 8.dp,
    val lg  : Dp = 12.dp,
    val xl  : Dp = 16.dp,
    val xxl : Dp = 24.dp,
    val xxxl: Dp = 32.dp,

    /* ───────────── Screen ───────────── */

    val screenPadding      : Dp = 16.dp,
    val screenHorizontal   : Dp = 16.dp,
    val screenVertical     : Dp = 24.dp,
    val screenPaddingHalf  : Dp = 8.dp,
    val screenPaddingDouble: Dp = 32.dp,
    val screenPaddingTriple: Dp = 48.dp,

    /* ───────────── Sections ───────────── */

    val sectionSpacing        : Dp = 32.dp,
    val subsectionSpacing     : Dp = 24.dp,
    val sectionSpacingCompact : Dp = 24.dp,
    val sectionSpacingExpanded: Dp = 40.dp,
    val sectionSpacingDouble  : Dp = 64.dp,

    /* ───────────── Cards ───────────── */

    val cardPadding         : Dp = 16.dp,
    val cardSpacing         : Dp = 12.dp,
    val cardPaddingCompact  : Dp = 12.dp,
    val cardPaddingExpanded : Dp = 24.dp,
    val cardPaddingDouble   : Dp = 32.dp,
    val cardSpacingCompact  : Dp = 8.dp,
    val cardSpacingExpanded : Dp = 16.dp,

    /* ───────────── Lists / Items ───────────── */

    val itemSpacing                : Dp = 8.dp,
    val itemVerticalPadding        : Dp = 12.dp,
    val itemHorizontalPadding      : Dp = 16.dp,
    val itemSpacingCompact         : Dp = 4.dp,
    val itemSpacingExpanded        : Dp = 16.dp,
    val itemVerticalPaddingCompact : Dp = 8.dp,
    val itemVerticalPaddingExpanded: Dp = 16.dp,

    /* ───────────── Components ───────────── */

    val componentPaddingXs    : Dp = 4.dp,
    val componentPaddingSm    : Dp = 8.dp,
    val componentPaddingMd    : Dp = 12.dp,
    val componentPaddingLg    : Dp = 16.dp,
    val componentPaddingXl    : Dp = 24.dp,
    val componentPaddingDouble: Dp = 32.dp,

    /* ───────────── Inline / Text ───────────── */

    val inlineSpacingXs: Dp = 4.dp,
    val inlineSpacingSm: Dp = 8.dp,
    val inlineSpacingMd: Dp = 12.dp,
    val inlineSpacingLg: Dp = 16.dp,
    val inlineSpacingXl: Dp = 24.dp,

    /* ───────────── Buttons ───────────── */

    val buttonPaddingVertical          : Dp = 14.dp,
    val buttonPaddingHorizontal        : Dp = 24.dp,
    val buttonSpacing                  : Dp = 8.dp,
    val buttonPaddingVerticalCompact   : Dp = 10.dp,
    val buttonPaddingVerticalExpanded  : Dp = 18.dp,
    val buttonPaddingHorizontalCompact : Dp = 16.dp,
    val buttonPaddingHorizontalExpanded: Dp = 32.dp,
    val buttonSpacingDouble            : Dp = 16.dp,

    /* ───────────── Dialogs / Sheets ───────────── */

    val dialogPadding         : Dp = 24.dp,
    val sheetPadding          : Dp = 16.dp,
    val dialogPaddingCompact  : Dp = 16.dp,
    val dialogPaddingExpanded : Dp = 32.dp,
    val sheetPaddingCompact   : Dp = 12.dp,
    val sheetPaddingExpanded  : Dp = 24.dp,
)
