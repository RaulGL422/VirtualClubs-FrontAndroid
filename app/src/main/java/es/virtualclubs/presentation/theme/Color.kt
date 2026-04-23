package es.virtualclubs.presentation.theme

import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────
//  Brand tokens — Stadium Design System
// ─────────────────────────────────────────────

// Primary — Electric Blue
val VCBlue900 = Color(0xFF071580)
val VCBlue800 = Color(0xFF0D29B8)
val VCBlue700 = Color(0xFF1236D4)
val VCBlue600 = Color(0xFF1A46E8)
val VCBlue500 = Color(0xFF3A62F0)
val VCBlue400 = Color(0xFF6282FF)
val VCBlue300 = Color(0xFF8FAAFF)
val VCBlue200 = Color(0xFFB8CCFF)
val VCBlue100 = Color(0xFFD9E4FF)
val VCBlue50  = Color(0xFFEDF1FF)

// Secondary — Fire Orange
val VCOrange900 = Color(0xFF5C1500)
val VCOrange800 = Color(0xFF8A2000)
val VCOrange700 = Color(0xFFB83400)
val VCOrange600 = Color(0xFFD44200)
val VCOrange500 = Color(0xFFEA5410)
val VCOrange400 = Color(0xFFFF7540)
val VCOrange300 = Color(0xFFFF9870)
val VCOrange200 = Color(0xFFFFBDA8)
val VCOrange100 = Color(0xFFFFDDD3)
val VCOrange50  = Color(0xFFFFF2EE)

// Tertiary — Stadium Green
val VCGreen900 = Color(0xFF003320)
val VCGreen800 = Color(0xFF004D30)
val VCGreen700 = Color(0xFF006A3D)
val VCGreen600 = Color(0xFF008750)
val VCGreen500 = Color(0xFF00A462)
val VCGreen400 = Color(0xFF00C478)
val VCGreen300 = Color(0xFF4DD99C)
val VCGreen200 = Color(0xFF96ECC5)
val VCGreen100 = Color(0xFFC0F5DC)
val VCGreen50  = Color(0xFFE6FBF2)

// Neutral — Cool Blue-Gray
val VCNeutral950 = Color(0xFF070C18)
val VCNeutral900 = Color(0xFF0C1424)
val VCNeutral850 = Color(0xFF111C33)
val VCNeutral800 = Color(0xFF162240)
val VCNeutral750 = Color(0xFF1D2D52)
val VCNeutral700 = Color(0xFF263862)
val VCNeutral600 = Color(0xFF3D5280)
val VCNeutral500 = Color(0xFF5C72A0)
val VCNeutral400 = Color(0xFF8498C0)
val VCNeutral300 = Color(0xFFADBDD8)
val VCNeutral200 = Color(0xFFD0DBEC)
val VCNeutral150 = Color(0xFFE2EAF5)
val VCNeutral100 = Color(0xFFEDF2F9)
val VCNeutral50  = Color(0xFFF5F8FD)

// ─────────────────────────────────────────────
//  Light scheme — Standard
// ─────────────────────────────────────────────

val primaryLight               = VCBlue600
val onPrimaryLight             = Color(0xFFFFFFFF)
val primaryContainerLight      = VCBlue100
val onPrimaryContainerLight    = VCBlue900

val secondaryLight             = VCOrange700
val onSecondaryLight           = Color(0xFFFFFFFF)
val secondaryContainerLight    = VCOrange100
val onSecondaryContainerLight  = VCOrange900

val tertiaryLight              = VCGreen700
val onTertiaryLight            = Color(0xFFFFFFFF)
val tertiaryContainerLight     = VCGreen100
val onTertiaryContainerLight   = VCGreen900

val errorLight                 = Color(0xFFB3000F)
val onErrorLight               = Color(0xFFFFFFFF)
val errorContainerLight        = Color(0xFFFFDAD6)
val onErrorContainerLight      = Color(0xFF690005)

val backgroundLight            = VCNeutral50
val onBackgroundLight          = VCNeutral950
val surfaceLight               = Color(0xFFFFFFFF)
val onSurfaceLight             = VCNeutral950
val surfaceVariantLight        = VCNeutral150
val onSurfaceVariantLight      = VCNeutral700
val outlineLight               = VCNeutral400
val outlineVariantLight        = VCNeutral200
val scrimLight                 = Color(0xFF000000)
val inverseSurfaceLight        = VCNeutral900
val inverseOnSurfaceLight      = VCNeutral100
val inversePrimaryLight        = VCBlue300
val surfaceDimLight            = VCNeutral200
val surfaceBrightLight         = Color(0xFFFFFFFF)
val surfaceContainerLowestLight   = Color(0xFFFFFFFF)
val surfaceContainerLowLight      = VCNeutral50
val surfaceContainerLight         = VCNeutral100
val surfaceContainerHighLight     = VCNeutral150
val surfaceContainerHighestLight  = VCNeutral200

// ─────────────────────────────────────────────
//  Light scheme — Medium contrast
// ─────────────────────────────────────────────

val primaryLightMediumContrast              = VCBlue700
val onPrimaryLightMediumContrast            = Color(0xFFFFFFFF)
val primaryContainerLightMediumContrast     = VCBlue500
val onPrimaryContainerLightMediumContrast   = Color(0xFFFFFFFF)

val secondaryLightMediumContrast            = VCOrange800
val onSecondaryLightMediumContrast          = Color(0xFFFFFFFF)
val secondaryContainerLightMediumContrast   = VCOrange600
val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)

val tertiaryLightMediumContrast             = VCGreen800
val onTertiaryLightMediumContrast           = Color(0xFFFFFFFF)
val tertiaryContainerLightMediumContrast    = VCGreen600
val onTertiaryContainerLightMediumContrast  = Color(0xFFFFFFFF)

val errorLightMediumContrast               = Color(0xFF740006)
val onErrorLightMediumContrast             = Color(0xFFFFFFFF)
val errorContainerLightMediumContrast      = Color(0xFFCF2C27)
val onErrorContainerLightMediumContrast    = Color(0xFFFFFFFF)

val backgroundLightMediumContrast          = VCNeutral50
val onBackgroundLightMediumContrast        = VCNeutral950
val surfaceLightMediumContrast             = Color(0xFFFFFFFF)
val onSurfaceLightMediumContrast           = VCNeutral950
val surfaceVariantLightMediumContrast      = VCNeutral150
val onSurfaceVariantLightMediumContrast    = VCNeutral800
val outlineLightMediumContrast             = VCNeutral600
val outlineVariantLightMediumContrast      = VCNeutral400
val scrimLightMediumContrast               = Color(0xFF000000)
val inverseSurfaceLightMediumContrast      = VCNeutral900
val inverseOnSurfaceLightMediumContrast    = VCNeutral100
val inversePrimaryLightMediumContrast      = VCBlue300
val surfaceDimLightMediumContrast          = VCNeutral200
val surfaceBrightLightMediumContrast       = Color(0xFFFFFFFF)
val surfaceContainerLowestLightMediumContrast  = Color(0xFFFFFFFF)
val surfaceContainerLowLightMediumContrast     = VCNeutral50
val surfaceContainerLightMediumContrast        = VCNeutral100
val surfaceContainerHighLightMediumContrast    = VCNeutral150
val surfaceContainerHighestLightMediumContrast = VCNeutral200

// ─────────────────────────────────────────────
//  Light scheme — High contrast
// ─────────────────────────────────────────────

val primaryLightHighContrast              = VCBlue900
val onPrimaryLightHighContrast            = Color(0xFFFFFFFF)
val primaryContainerLightHighContrast     = VCBlue700
val onPrimaryContainerLightHighContrast   = Color(0xFFFFFFFF)

val secondaryLightHighContrast            = VCOrange900
val onSecondaryLightHighContrast          = Color(0xFFFFFFFF)
val secondaryContainerLightHighContrast   = VCOrange700
val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)

val tertiaryLightHighContrast             = VCGreen900
val onTertiaryLightHighContrast           = Color(0xFFFFFFFF)
val tertiaryContainerLightHighContrast    = VCGreen700
val onTertiaryContainerLightHighContrast  = Color(0xFFFFFFFF)

val errorLightHighContrast                = Color(0xFF600004)
val onErrorLightHighContrast              = Color(0xFFFFFFFF)
val errorContainerLightHighContrast       = Color(0xFF98000A)
val onErrorContainerLightHighContrast     = Color(0xFFFFFFFF)

val backgroundLightHighContrast           = Color(0xFFFFFFFF)
val onBackgroundLightHighContrast         = Color(0xFF000000)
val surfaceLightHighContrast              = Color(0xFFFFFFFF)
val onSurfaceLightHighContrast            = Color(0xFF000000)
val surfaceVariantLightHighContrast       = VCNeutral150
val onSurfaceVariantLightHighContrast     = Color(0xFF000000)
val outlineLightHighContrast              = VCNeutral800
val outlineVariantLightHighContrast       = VCNeutral600
val scrimLightHighContrast                = Color(0xFF000000)
val inverseSurfaceLightHighContrast       = VCNeutral950
val inverseOnSurfaceLightHighContrast     = Color(0xFFFFFFFF)
val inversePrimaryLightHighContrast       = VCBlue200
val surfaceDimLightHighContrast           = VCNeutral200
val surfaceBrightLightHighContrast        = Color(0xFFFFFFFF)
val surfaceContainerLowestLightHighContrast  = Color(0xFFFFFFFF)
val surfaceContainerLowLightHighContrast     = VCNeutral50
val surfaceContainerLightHighContrast        = VCNeutral100
val surfaceContainerHighLightHighContrast    = VCNeutral150
val surfaceContainerHighestLightHighContrast = VCNeutral200

// ─────────────────────────────────────────────
//  Dark scheme — Standard
// ─────────────────────────────────────────────

val primaryDark               = VCBlue300
val onPrimaryDark             = VCBlue900
val primaryContainerDark      = VCBlue800
val onPrimaryContainerDark    = VCBlue100

val secondaryDark             = VCOrange300
val onSecondaryDark           = VCOrange900
val secondaryContainerDark    = VCOrange800
val onSecondaryContainerDark  = VCOrange100

val tertiaryDark              = VCGreen300
val onTertiaryDark            = VCGreen900
val tertiaryContainerDark     = VCGreen800
val onTertiaryContainerDark   = VCGreen100

val errorDark                 = Color(0xFFFFB4AB)
val onErrorDark               = Color(0xFF690005)
val errorContainerDark        = Color(0xFF93000A)
val onErrorContainerDark      = Color(0xFFFFDAD6)

val backgroundDark            = VCNeutral950
val onBackgroundDark          = VCNeutral100
val surfaceDark               = VCNeutral950
val onSurfaceDark             = VCNeutral100
val surfaceVariantDark        = VCNeutral800
val onSurfaceVariantDark      = VCNeutral200
val outlineDark               = VCNeutral500
val outlineVariantDark        = VCNeutral800
val scrimDark                 = Color(0xFF000000)
val inverseSurfaceDark        = VCNeutral100
val inverseOnSurfaceDark      = VCNeutral900
val inversePrimaryDark        = VCBlue600
val surfaceDimDark            = VCNeutral950
val surfaceBrightDark         = VCNeutral750
val surfaceContainerLowestDark   = Color(0xFF030711)
val surfaceContainerLowDark      = VCNeutral900
val surfaceContainerDark         = VCNeutral850
val surfaceContainerHighDark     = VCNeutral800
val surfaceContainerHighestDark  = VCNeutral750

// ─────────────────────────────────────────────
//  Dark scheme — Medium contrast
// ─────────────────────────────────────────────

val primaryDarkMediumContrast              = VCBlue200
val onPrimaryDarkMediumContrast            = VCBlue900
val primaryContainerDarkMediumContrast     = VCBlue400
val onPrimaryContainerDarkMediumContrast   = Color(0xFF000000)

val secondaryDarkMediumContrast            = VCOrange200
val onSecondaryDarkMediumContrast          = VCOrange900
val secondaryContainerDarkMediumContrast   = VCOrange500
val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)

val tertiaryDarkMediumContrast             = VCGreen200
val onTertiaryDarkMediumContrast           = VCGreen900
val tertiaryContainerDarkMediumContrast    = VCGreen400
val onTertiaryContainerDarkMediumContrast  = Color(0xFF000000)

val errorDarkMediumContrast                = Color(0xFFFFD2CC)
val onErrorDarkMediumContrast              = Color(0xFF540003)
val errorContainerDarkMediumContrast       = Color(0xFFFF5449)
val onErrorContainerDarkMediumContrast     = Color(0xFF000000)

val backgroundDarkMediumContrast           = VCNeutral950
val onBackgroundDarkMediumContrast         = VCNeutral100
val surfaceDarkMediumContrast              = VCNeutral950
val onSurfaceDarkMediumContrast            = Color(0xFFFFFFFF)
val surfaceVariantDarkMediumContrast       = VCNeutral800
val onSurfaceVariantDarkMediumContrast     = VCNeutral150
val outlineDarkMediumContrast              = VCNeutral300
val outlineVariantDarkMediumContrast       = VCNeutral500
val scrimDarkMediumContrast                = Color(0xFF000000)
val inverseSurfaceDarkMediumContrast       = VCNeutral100
val inverseOnSurfaceDarkMediumContrast     = VCNeutral850
val inversePrimaryDarkMediumContrast       = VCBlue700
val surfaceDimDarkMediumContrast           = VCNeutral950
val surfaceBrightDarkMediumContrast        = VCNeutral700
val surfaceContainerLowestDarkMediumContrast  = Color(0xFF030711)
val surfaceContainerLowDarkMediumContrast     = VCNeutral900
val surfaceContainerDarkMediumContrast        = VCNeutral850
val surfaceContainerHighDarkMediumContrast    = VCNeutral800
val surfaceContainerHighestDarkMediumContrast = VCNeutral750

// ─────────────────────────────────────────────
//  Dark scheme — High contrast
// ─────────────────────────────────────────────

val primaryDarkHighContrast              = Color(0xFFEEF2FF)
val onPrimaryDarkHighContrast            = Color(0xFF000000)
val primaryContainerDarkHighContrast     = VCBlue300
val onPrimaryContainerDarkHighContrast   = Color(0xFF000000)

val secondaryDarkHighContrast            = Color(0xFFFFF2EE)
val onSecondaryDarkHighContrast          = Color(0xFF000000)
val secondaryContainerDarkHighContrast   = VCOrange300
val onSecondaryContainerDarkHighContrast = Color(0xFF000000)

val tertiaryDarkHighContrast             = Color(0xFFE6FBF2)
val onTertiaryDarkHighContrast           = Color(0xFF000000)
val tertiaryContainerDarkHighContrast    = VCGreen300
val onTertiaryContainerDarkHighContrast  = Color(0xFF000000)

val errorDarkHighContrast                = Color(0xFFFFECE9)
val onErrorDarkHighContrast              = Color(0xFF000000)
val errorContainerDarkHighContrast       = Color(0xFFFFAEA4)
val onErrorContainerDarkHighContrast     = Color(0xFF220001)

val backgroundDarkHighContrast           = Color(0xFF000000)
val onBackgroundDarkHighContrast         = Color(0xFFFFFFFF)
val surfaceDarkHighContrast              = Color(0xFF000000)
val onSurfaceDarkHighContrast            = Color(0xFFFFFFFF)
val surfaceVariantDarkHighContrast       = VCNeutral900
val onSurfaceVariantDarkHighContrast     = Color(0xFFFFFFFF)
val outlineDarkHighContrast              = VCNeutral200
val outlineVariantDarkHighContrast       = VCNeutral400
val scrimDarkHighContrast                = Color(0xFF000000)
val inverseSurfaceDarkHighContrast       = Color(0xFFFFFFFF)
val inverseOnSurfaceDarkHighContrast     = Color(0xFF000000)
val inversePrimaryDarkHighContrast       = VCBlue800
val surfaceDimDarkHighContrast           = Color(0xFF000000)
val surfaceBrightDarkHighContrast        = VCNeutral800
val surfaceContainerLowestDarkHighContrast  = Color(0xFF000000)
val surfaceContainerLowDarkHighContrast     = VCNeutral950
val surfaceContainerDarkHighContrast        = VCNeutral900
val surfaceContainerHighDarkHighContrast    = VCNeutral850
val surfaceContainerHighestDarkHighContrast = VCNeutral800
