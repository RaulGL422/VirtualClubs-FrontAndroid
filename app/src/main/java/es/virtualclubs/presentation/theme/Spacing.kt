package es.virtualclubs.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(

  /* ───────────── Screen ───────────── */

  // Base
  val screenPadding: Dp = 16.dp,
  val screenHorizontal: Dp = 16.dp,
  val screenVertical: Dp = 24.dp,

  // Multipliers
  val screenPaddingHalf: Dp = 8.dp,
  val screenPaddingDouble: Dp = 32.dp,
  val screenPaddingTriple: Dp = 48.dp,

  /* ───────────── Sections ───────────── */

  // Base
  val sectionSpacing: Dp = 32.dp,
  val subsectionSpacing: Dp = 24.dp,

  // Variants
  val sectionSpacingCompact: Dp = 24.dp,
  val sectionSpacingExpanded: Dp = 40.dp,
  val sectionSpacingDouble: Dp = 64.dp,

  /* ───────────── Cards ───────────── */

  // Base
  val cardPadding: Dp = 16.dp,
  val cardSpacing: Dp = 12.dp,

  // Variants
  val cardPaddingCompact: Dp = 12.dp,
  val cardPaddingExpanded: Dp = 24.dp,
  val cardPaddingDouble: Dp = 32.dp,

  val cardSpacingCompact: Dp = 8.dp,
  val cardSpacingExpanded: Dp = 16.dp,

  /* ───────────── Lists / Items ───────────── */

  // Base
  val itemSpacing: Dp = 8.dp,
  val itemVerticalPadding: Dp = 12.dp,
  val itemHorizontalPadding: Dp = 16.dp,

  // Variants
  val itemSpacingCompact: Dp = 4.dp,
  val itemSpacingExpanded: Dp = 16.dp,

  val itemVerticalPaddingCompact: Dp = 8.dp,
  val itemVerticalPaddingExpanded: Dp = 16.dp,

  /* ───────────── Components ───────────── */

  // Base
  val componentPaddingSm: Dp = 8.dp,
  val componentPaddingMd: Dp = 12.dp,
  val componentPaddingLg: Dp = 16.dp,

  // Multipliers
  val componentPaddingXs: Dp = 4.dp,
  val componentPaddingXl: Dp = 24.dp,
  val componentPaddingDouble: Dp = 32.dp,

  /* ───────────── Inline / Text ───────────── */

  // Base
  val inlineSpacingXs: Dp = 4.dp,
  val inlineSpacingSm: Dp = 8.dp,
  val inlineSpacingMd: Dp = 12.dp,

  // Variants
  val inlineSpacingLg: Dp = 16.dp,
  val inlineSpacingXl: Dp = 24.dp,

  /* ───────────── Buttons ───────────── */

  // Base
  val buttonPaddingVertical: Dp = 12.dp,
  val buttonPaddingHorizontal: Dp = 16.dp,
  val buttonSpacing: Dp = 8.dp,

  // Variants
  val buttonPaddingVerticalCompact: Dp = 8.dp,
  val buttonPaddingVerticalExpanded: Dp = 16.dp,

  val buttonPaddingHorizontalCompact: Dp = 12.dp,
  val buttonPaddingHorizontalExpanded: Dp = 24.dp,

  val buttonSpacingDouble: Dp = 16.dp,

  /* ───────────── Dialogs / Sheets ───────────── */

  // Base
  val dialogPadding: Dp = 24.dp,
  val sheetPadding: Dp = 16.dp,

  // Variants
  val dialogPaddingCompact: Dp = 16.dp,
  val dialogPaddingExpanded: Dp = 32.dp,

  val sheetPaddingCompact: Dp = 12.dp,
  val sheetPaddingExpanded: Dp = 24.dp
)
