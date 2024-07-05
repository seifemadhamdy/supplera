package seifemadhamdy.supplera.main.shop.detail.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import seifemadhamdy.supplera.R

@Composable
fun ExpandableText(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
  fontSize: TextUnit = TextUnit.Unspecified,
  fontStyle: FontStyle? = null,
  fontWeight: FontWeight? = null,
  fontFamily: FontFamily? = null,
  letterSpacing: TextUnit = TextUnit.Unspecified,
  textDecoration: TextDecoration? = null,
  textAlign: TextAlign? = null,
  lineHeight: TextUnit = TextUnit.Unspecified,
  softWrap: Boolean = true,
  maxLinesBeforeExpansion: Int = 1,
  style: TextStyle = LocalTextStyle.current,
  expanderText: String = stringResource(id = R.string.more),
  expanderColor: Color = ButtonDefaults.textButtonColors().contentColor,
) {
  val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
  val expanderSizeState = remember { mutableStateOf<IntSize?>(null) }
  val expanderOffsetState = remember { mutableStateOf<Offset?>(null) }
  var isTextExpanded by remember { mutableStateOf(false) }
  val textLayoutResult = textLayoutResultState.value
  val expanderSize = expanderSizeState.value
  var visibleTextSubstring by remember(text) { mutableStateOf<String?>(null) }
  val expanderOffset = expanderOffsetState.value

  LaunchedEffect(text, isTextExpanded, textLayoutResult, expanderSize) {
    val lastLineIndex = maxLinesBeforeExpansion - 1

    if (
      !isTextExpanded &&
        textLayoutResult != null &&
        expanderSize != null &&
        lastLineIndex + 1 == textLayoutResult.lineCount &&
        textLayoutResult.isLineEllipsized(lastLineIndex)
    ) {
      var lastCharIndex =
        textLayoutResult.getLineEnd(lineIndex = lastLineIndex, visibleEnd = true) + 1

      var charRect: Rect

      do {
        lastCharIndex -= 1
        charRect = textLayoutResult.getCursorRect(lastCharIndex)
      } while (charRect.left > textLayoutResult.size.width - expanderSize.width)

      expanderOffsetState.value =
        Offset(x = charRect.left, y = charRect.bottom - expanderSize.height)
      visibleTextSubstring = text.substring(startIndex = 0, endIndex = lastCharIndex)
    }
  }

  Box(modifier = modifier) {
    Text(
      text = visibleTextSubstring ?: text,
      modifier =
        Modifier.animateContentSize()
          .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
          .drawWithContent {
            drawContent()

            if (!isTextExpanded && expanderOffset != null) {
              val contentDrawScopeLineHeight = size.height / maxLinesBeforeExpansion

              drawRect(
                brush = Brush.horizontalGradient(0f to Color.Black, 1f to Color.Unspecified),
                topLeft =
                  Offset(x = 0f, y = contentDrawScopeLineHeight * (maxLinesBeforeExpansion - 1)),
                size = Size(width = size.width, height = contentDrawScopeLineHeight),
                blendMode = BlendMode.DstIn,
              )
            }
          },
      color = color,
      fontSize = fontSize,
      fontStyle = fontStyle,
      fontWeight = fontWeight,
      fontFamily = fontFamily,
      letterSpacing = letterSpacing,
      textDecoration = textDecoration,
      textAlign = textAlign,
      lineHeight = lineHeight,
      overflow = TextOverflow.Ellipsis,
      softWrap = softWrap,
      maxLines = if (isTextExpanded) Int.MAX_VALUE else maxLinesBeforeExpansion,
      onTextLayout = { textLayoutResultState.value = it },
      style = style,
    )

    if (!isTextExpanded) {
      val density = LocalDensity.current

      Text(
        text = expanderText,
        modifier =
          Modifier.then(
              if (expanderOffset != null)
                Modifier.offset(
                    x = with(density) { expanderOffset.x.toDp() + 8.dp },
                    y = with(density) { expanderOffset.y.toDp() },
                  )
                  .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                  ) {
                    isTextExpanded = true
                    visibleTextSubstring = null
                  }
              else Modifier
            )
            .alpha(if (expanderOffset != null) 1f else 0f),
        color = expanderColor,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        softWrap = softWrap,
        onTextLayout = {
          expanderSizeState.value =
            IntSize(
              width = it.size.width + with(density) { 8.dp.toPx().toInt() },
              height = it.size.height,
            )
        },
        style = style,
      )
    }
  }
}
