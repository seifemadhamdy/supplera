package seifemadhamdy.supplera.main.basket.payment.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import seifemadhamdy.supplera.R

fun Color.toHsl(): FloatArray {
  val redComponent = red
  val greenComponent = green
  val blueComponent = blue
  val maxComponent = maxOf(redComponent, greenComponent, blueComponent)
  val minComponent = minOf(redComponent, greenComponent, blueComponent)
  val delta = maxComponent - minComponent
  val lightness = (maxComponent + minComponent) / 2
  val hue: Float
  val saturation: Float

  if (maxComponent == minComponent) {
    hue = 0f
    saturation = 0f
  } else {
    saturation =
      if (lightness > 0.5) delta / (2 - maxComponent - minComponent)
      else delta / (maxComponent + minComponent)
    hue =
      when (maxComponent) {
        redComponent -> 60 * ((greenComponent - blueComponent) / delta % 6)
        greenComponent -> 60 * ((blueComponent - redComponent) / delta + 2)
        else -> 60 * ((redComponent - greenComponent) / delta + 4)
      }
  }

  return floatArrayOf(hue.coerceIn(0f, 360f), saturation, lightness)
}

fun hslToColor(hue: Float, saturation: Float, lightness: Float): Color {
  val chroma = (1 - kotlin.math.abs(2 * lightness - 1)) * saturation
  val secondaryColorComponent = chroma * (1 - kotlin.math.abs((hue / 60) % 2 - 1))
  val matchValue = lightness - chroma / 2
  var red = matchValue
  var green = matchValue
  var blue = matchValue

  when ((hue.toInt() / 60) % 6) {
    0 -> {
      red += chroma
      green += secondaryColorComponent
    }
    1 -> {
      red += secondaryColorComponent
      green += chroma
    }
    2 -> {
      green += chroma
      blue += secondaryColorComponent
    }
    3 -> {
      green += secondaryColorComponent
      blue += chroma
    }
    4 -> {
      red += secondaryColorComponent
      blue += chroma
    }
    5 -> {
      red += chroma
      blue += secondaryColorComponent
    }
  }

  return Color(red = red, green = green, blue = blue)
}

fun Color.setSaturation(newSaturation: Float): Color {
  val hslValues = this.toHsl()
  return hslToColor(hslValues[0], newSaturation.coerceIn(0f, 1f), hslValues[2])
}

@Composable
fun BankCardBackground(baseColor: Color) {
  val colorSaturation75 = baseColor.setSaturation(0.75f)
  val colorSaturation50 = baseColor.setSaturation(0.5f)

  Canvas(modifier = Modifier.fillMaxSize().background(baseColor)) {
    drawCircle(
      color = colorSaturation75,
      center = Offset(x = size.width * 0.2f, y = size.height * 0.6f),
      radius = size.minDimension * 0.85f,
    )

    drawCircle(
      color = colorSaturation50,
      center = Offset(x = size.width * 0.1f, y = size.height * 0.3f),
      radius = size.minDimension * 0.75f,
    )
  }
}

@Composable
fun BankCardNumber(cardNumber: String) {
  Row(
    modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    repeat(4) {
      val startIndex = it * 4

      Text(
        text =
          (cardNumber + "X".repeat(16 - cardNumber.length)).substring(
            startIndex = startIndex,
            endIndex = startIndex + 4,
          ),
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.titleLarge,
      )
    }
  }
}

@Composable
fun SpaceWrapper(
  modifier: Modifier = Modifier,
  space: Dp,
  top: Boolean = false,
  right: Boolean = false,
  bottom: Boolean = false,
  left: Boolean = false,
  content: @Composable BoxScope.() -> Unit,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier =
      modifier
        .then(if (top) Modifier.padding(top = space) else Modifier)
        .then(if (right) Modifier.padding(end = space) else Modifier)
        .then(if (bottom) Modifier.padding(bottom = space) else Modifier)
        .then(if (left) Modifier.padding(start = space) else Modifier),
  ) {
    content()
  }
}

@Composable
fun BankCardPreviewCard(
  modifier: Modifier = Modifier,
  cardColor: Color = MaterialTheme.colorScheme.primaryContainer,
  cardNumber: String = "",
  cardholder: String = "",
  expirationMonth: String = "",
  expirationYear: String = "",
  cvv: String = "",
  brand: String = "",
) {
  Card(
    modifier = modifier.fillMaxWidth().aspectRatio(1.586f),
    colors = CardDefaults.cardColors(containerColor = cardColor),
  ) {
    Box {
      BankCardBackground(baseColor = cardColor)
      BankCardNumber(cardNumber = cardNumber)

      SpaceWrapper(
        modifier = Modifier.align(Alignment.TopStart),
        space = 32.dp,
        top = true,
        left = true,
      ) {
        Text(
          text = cardholder.ifBlank { stringResource(id = R.string.cardholder) }.uppercase(),
          fontWeight = FontWeight.Medium,
          style = MaterialTheme.typography.titleMedium,
        )
      }

      SpaceWrapper(
        modifier = Modifier.align(Alignment.BottomStart),
        space = 32.dp,
        bottom = true,
        left = true,
      ) {
        Row {
          Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = stringResource(id = R.string.exp_date).uppercase(),
              color = LocalContentColor.current.copy(alpha = 0.6f),
              style = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text =
                "${(expirationMonth + "X".repeat(4 - expirationMonth.length)).substring(startIndex = 0, endIndex = 2)} / ${(expirationYear + "X".repeat(4 - expirationYear.length)).substring(startIndex = 0, endIndex = 2)}",
              fontWeight = FontWeight.Medium,
              style = MaterialTheme.typography.titleMedium,
            )
          }

          Spacer(modifier = Modifier.width(16.dp))

          Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = stringResource(id = R.string.cvv).uppercase(),
              color = LocalContentColor.current.copy(alpha = 0.6f),
              style = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = (cvv + "X".repeat(3 - cvv.length)).substring(startIndex = 0, endIndex = 3),
              fontWeight = FontWeight.Medium,
              style = MaterialTheme.typography.titleMedium,
            )
          }
        }
      }

      SpaceWrapper(
        modifier = Modifier.align(Alignment.BottomEnd),
        space = 32.dp,
        bottom = true,
        right = true,
      ) {
        Text(
          text = brand,
          fontStyle = FontStyle.Italic,
          fontWeight = FontWeight.SemiBold,
          style = MaterialTheme.typography.headlineMedium,
        )
      }
    }
  }
}
