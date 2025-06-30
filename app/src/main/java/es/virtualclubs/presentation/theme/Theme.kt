package es.virtualclubs.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import es.virtualclubs.data.local.datastore.AppPreferences
import es.iesfernandoaguilar.ui.theme.AppShapes
import es.iesfernandoaguilar.ui.theme.AppTypography
import es.iesfernandoaguilar.ui.theme.backgroundDark
import es.iesfernandoaguilar.ui.theme.backgroundDarkHighContrast
import es.iesfernandoaguilar.ui.theme.backgroundDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.backgroundLight
import es.iesfernandoaguilar.ui.theme.backgroundLightHighContrast
import es.iesfernandoaguilar.ui.theme.backgroundLightMediumContrast
import es.iesfernandoaguilar.ui.theme.errorContainerDark
import es.iesfernandoaguilar.ui.theme.errorContainerDarkHighContrast
import es.iesfernandoaguilar.ui.theme.errorContainerDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.errorContainerLight
import es.iesfernandoaguilar.ui.theme.errorContainerLightHighContrast
import es.iesfernandoaguilar.ui.theme.errorContainerLightMediumContrast
import es.iesfernandoaguilar.ui.theme.errorDark
import es.iesfernandoaguilar.ui.theme.errorDarkHighContrast
import es.iesfernandoaguilar.ui.theme.errorDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.errorLight
import es.iesfernandoaguilar.ui.theme.errorLightHighContrast
import es.iesfernandoaguilar.ui.theme.errorLightMediumContrast
import es.iesfernandoaguilar.ui.theme.inverseOnSurfaceDark
import es.iesfernandoaguilar.ui.theme.inverseOnSurfaceDarkHighContrast
import es.iesfernandoaguilar.ui.theme.inverseOnSurfaceDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.inverseOnSurfaceLight
import es.iesfernandoaguilar.ui.theme.inverseOnSurfaceLightHighContrast
import es.iesfernandoaguilar.ui.theme.inverseOnSurfaceLightMediumContrast
import es.iesfernandoaguilar.ui.theme.inversePrimaryDark
import es.iesfernandoaguilar.ui.theme.inversePrimaryDarkHighContrast
import es.iesfernandoaguilar.ui.theme.inversePrimaryDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.inversePrimaryLight
import es.iesfernandoaguilar.ui.theme.inversePrimaryLightHighContrast
import es.iesfernandoaguilar.ui.theme.inversePrimaryLightMediumContrast
import es.iesfernandoaguilar.ui.theme.inverseSurfaceDark
import es.iesfernandoaguilar.ui.theme.inverseSurfaceDarkHighContrast
import es.iesfernandoaguilar.ui.theme.inverseSurfaceDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.inverseSurfaceLight
import es.iesfernandoaguilar.ui.theme.inverseSurfaceLightHighContrast
import es.iesfernandoaguilar.ui.theme.inverseSurfaceLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onBackgroundDark
import es.iesfernandoaguilar.ui.theme.onBackgroundDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onBackgroundDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onBackgroundLight
import es.iesfernandoaguilar.ui.theme.onBackgroundLightHighContrast
import es.iesfernandoaguilar.ui.theme.onBackgroundLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onErrorContainerDark
import es.iesfernandoaguilar.ui.theme.onErrorContainerDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onErrorContainerDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onErrorContainerLight
import es.iesfernandoaguilar.ui.theme.onErrorContainerLightHighContrast
import es.iesfernandoaguilar.ui.theme.onErrorContainerLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onErrorDark
import es.iesfernandoaguilar.ui.theme.onErrorDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onErrorDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onErrorLight
import es.iesfernandoaguilar.ui.theme.onErrorLightHighContrast
import es.iesfernandoaguilar.ui.theme.onErrorLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onPrimaryContainerDark
import es.iesfernandoaguilar.ui.theme.onPrimaryContainerDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onPrimaryContainerDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onPrimaryContainerLight
import es.iesfernandoaguilar.ui.theme.onPrimaryContainerLightHighContrast
import es.iesfernandoaguilar.ui.theme.onPrimaryContainerLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onPrimaryDark
import es.iesfernandoaguilar.ui.theme.onPrimaryDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onPrimaryDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onPrimaryLight
import es.iesfernandoaguilar.ui.theme.onPrimaryLightHighContrast
import es.iesfernandoaguilar.ui.theme.onPrimaryLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onSecondaryContainerDark
import es.iesfernandoaguilar.ui.theme.onSecondaryContainerDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onSecondaryContainerDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onSecondaryContainerLight
import es.iesfernandoaguilar.ui.theme.onSecondaryContainerLightHighContrast
import es.iesfernandoaguilar.ui.theme.onSecondaryContainerLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onSecondaryDark
import es.iesfernandoaguilar.ui.theme.onSecondaryDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onSecondaryDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onSecondaryLight
import es.iesfernandoaguilar.ui.theme.onSecondaryLightHighContrast
import es.iesfernandoaguilar.ui.theme.onSecondaryLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onSurfaceDark
import es.iesfernandoaguilar.ui.theme.onSurfaceDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onSurfaceDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onSurfaceLight
import es.iesfernandoaguilar.ui.theme.onSurfaceLightHighContrast
import es.iesfernandoaguilar.ui.theme.onSurfaceLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onSurfaceVariantDark
import es.iesfernandoaguilar.ui.theme.onSurfaceVariantDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onSurfaceVariantDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onSurfaceVariantLight
import es.iesfernandoaguilar.ui.theme.onSurfaceVariantLightHighContrast
import es.iesfernandoaguilar.ui.theme.onSurfaceVariantLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onTertiaryContainerDark
import es.iesfernandoaguilar.ui.theme.onTertiaryContainerDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onTertiaryContainerDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onTertiaryContainerLight
import es.iesfernandoaguilar.ui.theme.onTertiaryContainerLightHighContrast
import es.iesfernandoaguilar.ui.theme.onTertiaryContainerLightMediumContrast
import es.iesfernandoaguilar.ui.theme.onTertiaryDark
import es.iesfernandoaguilar.ui.theme.onTertiaryDarkHighContrast
import es.iesfernandoaguilar.ui.theme.onTertiaryDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.onTertiaryLight
import es.iesfernandoaguilar.ui.theme.onTertiaryLightHighContrast
import es.iesfernandoaguilar.ui.theme.onTertiaryLightMediumContrast
import es.iesfernandoaguilar.ui.theme.outlineDark
import es.iesfernandoaguilar.ui.theme.outlineDarkHighContrast
import es.iesfernandoaguilar.ui.theme.outlineDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.outlineLight
import es.iesfernandoaguilar.ui.theme.outlineLightHighContrast
import es.iesfernandoaguilar.ui.theme.outlineLightMediumContrast
import es.iesfernandoaguilar.ui.theme.outlineVariantDark
import es.iesfernandoaguilar.ui.theme.outlineVariantDarkHighContrast
import es.iesfernandoaguilar.ui.theme.outlineVariantDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.outlineVariantLight
import es.iesfernandoaguilar.ui.theme.outlineVariantLightHighContrast
import es.iesfernandoaguilar.ui.theme.outlineVariantLightMediumContrast
import es.iesfernandoaguilar.ui.theme.primaryContainerDark
import es.iesfernandoaguilar.ui.theme.primaryContainerDarkHighContrast
import es.iesfernandoaguilar.ui.theme.primaryContainerDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.primaryContainerLight
import es.iesfernandoaguilar.ui.theme.primaryContainerLightHighContrast
import es.iesfernandoaguilar.ui.theme.primaryContainerLightMediumContrast
import es.iesfernandoaguilar.ui.theme.primaryDark
import es.iesfernandoaguilar.ui.theme.primaryDarkHighContrast
import es.iesfernandoaguilar.ui.theme.primaryDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.primaryLight
import es.iesfernandoaguilar.ui.theme.primaryLightHighContrast
import es.iesfernandoaguilar.ui.theme.primaryLightMediumContrast
import es.iesfernandoaguilar.ui.theme.scaledTypography
import es.iesfernandoaguilar.ui.theme.scrimDark
import es.iesfernandoaguilar.ui.theme.scrimDarkHighContrast
import es.iesfernandoaguilar.ui.theme.scrimDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.scrimLight
import es.iesfernandoaguilar.ui.theme.scrimLightHighContrast
import es.iesfernandoaguilar.ui.theme.scrimLightMediumContrast
import es.iesfernandoaguilar.ui.theme.secondaryContainerDark
import es.iesfernandoaguilar.ui.theme.secondaryContainerDarkHighContrast
import es.iesfernandoaguilar.ui.theme.secondaryContainerDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.secondaryContainerLight
import es.iesfernandoaguilar.ui.theme.secondaryContainerLightHighContrast
import es.iesfernandoaguilar.ui.theme.secondaryContainerLightMediumContrast
import es.iesfernandoaguilar.ui.theme.secondaryDark
import es.iesfernandoaguilar.ui.theme.secondaryDarkHighContrast
import es.iesfernandoaguilar.ui.theme.secondaryDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.secondaryLight
import es.iesfernandoaguilar.ui.theme.secondaryLightHighContrast
import es.iesfernandoaguilar.ui.theme.secondaryLightMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceBrightDark
import es.iesfernandoaguilar.ui.theme.surfaceBrightDarkHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceBrightDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceBrightLight
import es.iesfernandoaguilar.ui.theme.surfaceBrightLightHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceBrightLightMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerDark
import es.iesfernandoaguilar.ui.theme.surfaceContainerDarkHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighDark
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighDarkHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighLight
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighLightHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighLightMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighestDark
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighestDarkHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighestDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighestLight
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighestLightHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerHighestLightMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerLight
import es.iesfernandoaguilar.ui.theme.surfaceContainerLightHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerLightMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowDark
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowDarkHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowLight
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowLightHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowLightMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowestDark
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowestDarkHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowestDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowestLight
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowestLightHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceContainerLowestLightMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceDark
import es.iesfernandoaguilar.ui.theme.surfaceDarkHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceDimDark
import es.iesfernandoaguilar.ui.theme.surfaceDimDarkHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceDimDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceDimLight
import es.iesfernandoaguilar.ui.theme.surfaceDimLightHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceDimLightMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceLight
import es.iesfernandoaguilar.ui.theme.surfaceLightHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceLightMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceVariantDark
import es.iesfernandoaguilar.ui.theme.surfaceVariantDarkHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceVariantDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.surfaceVariantLight
import es.iesfernandoaguilar.ui.theme.surfaceVariantLightHighContrast
import es.iesfernandoaguilar.ui.theme.surfaceVariantLightMediumContrast
import es.iesfernandoaguilar.ui.theme.tertiaryContainerDark
import es.iesfernandoaguilar.ui.theme.tertiaryContainerDarkHighContrast
import es.iesfernandoaguilar.ui.theme.tertiaryContainerDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.tertiaryContainerLight
import es.iesfernandoaguilar.ui.theme.tertiaryContainerLightHighContrast
import es.iesfernandoaguilar.ui.theme.tertiaryContainerLightMediumContrast
import es.iesfernandoaguilar.ui.theme.tertiaryDark
import es.iesfernandoaguilar.ui.theme.tertiaryDarkHighContrast
import es.iesfernandoaguilar.ui.theme.tertiaryDarkMediumContrast
import es.iesfernandoaguilar.ui.theme.tertiaryLight
import es.iesfernandoaguilar.ui.theme.tertiaryLightHighContrast
import es.iesfernandoaguilar.ui.theme.tertiaryLightMediumContrast

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Composable
fun VirtualClubsTheme(
    preferences: AppPreferences,
    content: @Composable () -> Unit
) {
    val isDarkTheme by preferences.darkThemeFlow.collectAsState(initial = null)
    val contrastType by preferences.contrastTypeFlow.collectAsState(initial = 0)
    val multiplier by preferences.fontSizeMultiplierFlow.collectAsState(initial = 1.0)

    // Verify if dark theme is true or search in system
    val colorScheme = if (isDarkTheme ?: isSystemInDarkTheme()) {
        // Dark Schemes
        when (contrastType) {
            0 -> darkScheme
            1 -> mediumContrastDarkColorScheme
            2 -> highContrastDarkColorScheme
            else -> darkScheme
        }
    } else {
        // Light Schemes
        when (contrastType) {
            0 -> lightScheme
            1 -> mediumContrastLightColorScheme
            2 -> highContrastLightColorScheme
            else -> lightScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = AppShapes,
        typography = scaledTypography(AppTypography, multiplier),
        content = content
    )
}
