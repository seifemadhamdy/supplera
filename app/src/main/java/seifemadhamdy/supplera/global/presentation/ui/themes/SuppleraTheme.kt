package seifemadhamdy.supplera.global.presentation.ui.themes

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_background
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_error
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_errorContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_inverseOnSurface
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_inversePrimary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_inverseSurface
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onBackground
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onError
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onErrorContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onPrimary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onPrimaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onSecondary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onSecondaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onSurface
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onSurfaceVariant
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onTertiary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onTertiaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_outline
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_outlineVariant
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_primary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_primaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_scrim
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_secondary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_secondaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_surface
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_surfaceTint
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_surfaceVariant
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_tertiary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_tertiaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_background
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_error
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_errorContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_inverseOnSurface
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_inversePrimary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_inverseSurface
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onBackground
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onError
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onErrorContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onPrimary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onPrimaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onSecondary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onSecondaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onSurface
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onSurfaceVariant
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onTertiary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onTertiaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_outline
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_outlineVariant
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_primary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_primaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_scrim
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_secondary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_secondaryContainer
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_surface
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_surfaceTint
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_surfaceVariant
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_tertiary
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_tertiaryContainer

@Composable
fun SuppleraTheme(useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  val view = LocalView.current

  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = 0
      window.navigationBarColor = 0
      val windowInsetsControllerCompat = WindowCompat.getInsetsController(window, view)
      windowInsetsControllerCompat.isAppearanceLightStatusBars = !useDarkTheme
      windowInsetsControllerCompat.isAppearanceLightNavigationBars = !useDarkTheme
    }
  }

  MaterialTheme(
    colorScheme =
      if (!useDarkTheme) {
        lightColorScheme(
          primary = md_theme_light_primary,
          onPrimary = md_theme_light_onPrimary,
          primaryContainer = md_theme_light_primaryContainer,
          onPrimaryContainer = md_theme_light_onPrimaryContainer,
          secondary = md_theme_light_secondary,
          onSecondary = md_theme_light_onSecondary,
          secondaryContainer = md_theme_light_secondaryContainer,
          onSecondaryContainer = md_theme_light_onSecondaryContainer,
          tertiary = md_theme_light_tertiary,
          onTertiary = md_theme_light_onTertiary,
          tertiaryContainer = md_theme_light_tertiaryContainer,
          onTertiaryContainer = md_theme_light_onTertiaryContainer,
          error = md_theme_light_error,
          errorContainer = md_theme_light_errorContainer,
          onError = md_theme_light_onError,
          onErrorContainer = md_theme_light_onErrorContainer,
          background = md_theme_light_background,
          onBackground = md_theme_light_onBackground,
          surface = md_theme_light_surface,
          onSurface = md_theme_light_onSurface,
          surfaceVariant = md_theme_light_surfaceVariant,
          onSurfaceVariant = md_theme_light_onSurfaceVariant,
          outline = md_theme_light_outline,
          inverseOnSurface = md_theme_light_inverseOnSurface,
          inverseSurface = md_theme_light_inverseSurface,
          inversePrimary = md_theme_light_inversePrimary,
          surfaceTint = md_theme_light_surfaceTint,
          outlineVariant = md_theme_light_outlineVariant,
          scrim = md_theme_light_scrim,
        )
      } else {
        darkColorScheme(
          primary = md_theme_dark_primary,
          onPrimary = md_theme_dark_onPrimary,
          primaryContainer = md_theme_dark_primaryContainer,
          onPrimaryContainer = md_theme_dark_onPrimaryContainer,
          secondary = md_theme_dark_secondary,
          onSecondary = md_theme_dark_onSecondary,
          secondaryContainer = md_theme_dark_secondaryContainer,
          onSecondaryContainer = md_theme_dark_onSecondaryContainer,
          tertiary = md_theme_dark_tertiary,
          onTertiary = md_theme_dark_onTertiary,
          tertiaryContainer = md_theme_dark_tertiaryContainer,
          onTertiaryContainer = md_theme_dark_onTertiaryContainer,
          error = md_theme_dark_error,
          errorContainer = md_theme_dark_errorContainer,
          onError = md_theme_dark_onError,
          onErrorContainer = md_theme_dark_onErrorContainer,
          background = md_theme_dark_background,
          onBackground = md_theme_dark_onBackground,
          surface = md_theme_dark_surface,
          onSurface = md_theme_dark_onSurface,
          surfaceVariant = md_theme_dark_surfaceVariant,
          onSurfaceVariant = md_theme_dark_onSurfaceVariant,
          outline = md_theme_dark_outline,
          inverseOnSurface = md_theme_dark_inverseOnSurface,
          inverseSurface = md_theme_dark_inverseSurface,
          inversePrimary = md_theme_dark_inversePrimary,
          surfaceTint = md_theme_dark_surfaceTint,
          outlineVariant = md_theme_dark_outlineVariant,
          scrim = md_theme_dark_scrim,
        )
      },
    shapes =
      Shapes(
        extraSmall = ShapeDefaults.ExtraSmall.copy(all = CornerSize(16.dp)),
        small = ShapeDefaults.Small.copy(all = CornerSize(20.dp)),
        medium = ShapeDefaults.Medium.copy(all = CornerSize(24.dp)),
        large = ShapeDefaults.Large.copy(all = CornerSize(28.dp)),
        extraLarge = ShapeDefaults.ExtraLarge.copy(all = CornerSize(32.dp)),
      ),
    typography =
      FontFamily(
          Font(R.font.poppins_thin, FontWeight.Thin),
          Font(R.font.poppins_thin_italic, FontWeight.Thin, FontStyle.Italic),
          Font(R.font.poppins_extra_light, FontWeight.ExtraLight),
          Font(R.font.poppins_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
          Font(R.font.poppins_light, FontWeight.Light),
          Font(R.font.poppins_light_italic, FontWeight.Light, FontStyle.Italic),
          Font(R.font.poppins_regular, FontWeight.Normal),
          Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
          Font(R.font.poppins_medium, FontWeight.Medium),
          Font(R.font.poppins_medium_italic, FontWeight.Medium, FontStyle.Italic),
          Font(R.font.poppins_semi_bold, FontWeight.SemiBold),
          Font(R.font.poppins_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
          Font(R.font.poppins_bold, FontWeight.Bold),
          Font(R.font.poppins_bold_italic, FontWeight.Bold, FontStyle.Italic),
          Font(R.font.poppins_extra_bold, FontWeight.ExtraBold),
          Font(R.font.poppins_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
          Font(R.font.poppins_black, FontWeight.Black),
          Font(R.font.poppins_black_italic, FontWeight.Black, FontStyle.Italic),
        )
        .run {
          Typography(
            displayLarge = Typography().displayLarge.copy(fontFamily = this),
            displayMedium = Typography().displayMedium.copy(fontFamily = this),
            displaySmall = Typography().displaySmall.copy(fontFamily = this),
            headlineLarge = Typography().headlineLarge.copy(fontFamily = this),
            headlineMedium = Typography().headlineMedium.copy(fontFamily = this),
            headlineSmall = Typography().headlineSmall.copy(fontFamily = this),
            titleLarge = Typography().titleLarge.copy(fontFamily = this),
            titleMedium = Typography().titleMedium.copy(fontFamily = this),
            titleSmall = Typography().titleSmall.copy(fontFamily = this),
            bodyLarge = Typography().bodyLarge.copy(fontFamily = this),
            bodyMedium = Typography().bodyMedium.copy(fontFamily = this),
            bodySmall = Typography().bodySmall.copy(fontFamily = this),
            labelLarge = Typography().labelLarge.copy(fontFamily = this),
            labelMedium = Typography().labelMedium.copy(fontFamily = this),
            labelSmall = Typography().labelSmall.copy(fontFamily = this),
          )
        },
    content = content,
  )
}
