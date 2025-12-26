package es.virtualclubs.presentation.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import es.virtualclubs.R
import es.virtualclubs.data.local.datastore.AppPreferences

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

val LocalAppPreferences = staticCompositionLocalOf<AppPreferences> {
  error("AppPreferences not provided")
}

val LocalSpacing = staticCompositionLocalOf { Spacing() }

val LocalSizes = staticCompositionLocalOf { Sizes() }

@Composable
private fun getColorScheme(preferences: AppPreferences): ColorScheme {
  val isDarkTheme by preferences.darkThemeFlow.collectAsState(initial = null)
  val contrastType by preferences.contrastTypeFlow.collectAsState(initial = 0)

  return if (isDarkTheme ?: isSystemInDarkTheme()) {
    when (contrastType) {
      0 -> darkScheme
      1 -> mediumContrastDarkColorScheme
      2 -> highContrastDarkColorScheme
      else -> darkScheme
    }
  } else {
    when (contrastType) {
      0 -> lightScheme
      1 -> mediumContrastLightColorScheme
      2 -> highContrastLightColorScheme
      else -> lightScheme
    }
  }
}

@Composable
private fun typography(preferences: AppPreferences): Typography {
  val multiplier by preferences.fontSizeMultiplierFlow.collectAsState(initial = 1.0)
  return scaledTypography(AppTypography, multiplier)
}

@Composable
fun VirtualClubsTheme(
  preferences: AppPreferences,
  content: @Composable () -> Unit
) {
  val spacing = Spacing()
  val sizes = Sizes()

  CompositionLocalProvider(
    LocalAppPreferences provides preferences,
    LocalSpacing provides spacing,
    LocalSizes provides sizes
  ) {
    MaterialTheme(
      colorScheme = VCTheme.colors,
      shapes = AppShapes,
      typography = VCTheme.typography,
      content = content
    )
  }
}

object VCTheme {
  val spacing: Spacing
    @Composable
    get() = LocalSpacing.current
  val appPreferences: AppPreferences
    @Composable
    get() = LocalAppPreferences.current
  val sizes: Sizes
    @Composable
    get() = LocalSizes.current
  val colors: ColorScheme
    @Composable
    get() = getColorScheme(appPreferences)
  val typography: Typography
    @Composable
    get() = typography(appPreferences)
  val shapes: VCShapes
    @Composable
    get() = VCShapes()
}

@Composable
fun getLargeLogo(): Painter {
  val preferences = LocalAppPreferences.current
  val isDarkTheme by preferences.darkThemeFlow.collectAsState(initial = null)
  return if (isDarkTheme ?: isSystemInDarkTheme()) {
    painterResource(id = R.drawable.logo_text_black)
  } else {
    painterResource(id = R.drawable.logo_text_white)
  }
}

@Composable
fun getLogo(): Painter {
  return painterResource(id = R.drawable.logo_whitout_text)
}

fun getAppVersion(context: Context): String {
  val packageInfo = context.packageManager.getPackageInfo(
    context.packageName,
    0
  )
  return packageInfo.versionName ?: "N/A"
}
