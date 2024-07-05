package seifemadhamdy.supplera.authentication.join.presentation.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun HighlightingText(
  modifier: Modifier = Modifier,
  stringsToHighlight: Map<String, () -> Unit>,
  text: String,
  highlightSpanStyle: SpanStyle = SpanStyle(),
  normalSpanStyle: SpanStyle = SpanStyle(),
  style: TextStyle = LocalTextStyle.current,
  softWrap: Boolean = true,
  overflow: TextOverflow = TextOverflow.Clip,
  maxLines: Int = Int.MAX_VALUE,
  onTextLayout: (TextLayoutResult) -> Unit = {},
) {
  val annotatedString = buildAnnotatedString {
    append(text)
    addStyle(style = normalSpanStyle, start = 0, end = text.length)

    for ((key, _) in stringsToHighlight) {
      val startIndex = text.indexOf(key)
      addStyle(style = highlightSpanStyle, start = startIndex, end = startIndex + key.length)
    }
  }

  ClickableText(
    text = annotatedString,
    modifier = modifier,
    style = style,
    softWrap = softWrap,
    overflow = overflow,
    maxLines = maxLines,
    onTextLayout = onTextLayout,
    onClick = { index ->
      for ((key, value) in stringsToHighlight) {
        val startIndex = annotatedString.indexOf(key)
        if (index in startIndex..startIndex + key.length) value.invoke()
      }
    },
  )
}
