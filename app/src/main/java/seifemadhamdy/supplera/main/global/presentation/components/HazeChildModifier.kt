package seifemadhamdy.supplera.main.global.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeChild

@Composable
fun Modifier.hazeChildModifier(
  hazeState: HazeState,
  shape: Shape = RectangleShape,
  tintColorAlpha: Float = 0.618f,
  tint: Color = MaterialTheme.colorScheme.surface,
  blurRadius: Dp = 20.dp,
  noiseFactor: Float = 0.0625f,
): Modifier =
  hazeChild(
    state = hazeState,
    shape = shape,
    style =
      HazeStyle(
        tint = if (tintColorAlpha in 0f..1f) tint.copy(alpha = tintColorAlpha) else tint,
        blurRadius = if (blurRadius >= 1.dp) blurRadius else 1.dp,
        noiseFactor = if (noiseFactor >= 0f) noiseFactor else 0f,
      ),
  )
