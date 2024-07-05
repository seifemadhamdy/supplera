package seifemadhamdy.supplera.main.shop.detail.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import seifemadhamdy.supplera.api.data.models.Highlight

@Composable
fun HighlightColumn(modifier: Modifier = Modifier, highlight: Highlight) {
  Column(
    modifier =
      modifier
        .fillMaxWidth()
        .clip(shape = CardDefaults.shape)
        .background(color = MaterialTheme.colorScheme.surfaceContainerHigh)
        .padding(all = 16.dp)
  ) {
    Text(
      text = highlight.title!!,
      fontWeight = FontWeight.Medium,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      style = MaterialTheme.typography.titleMedium,
    )

    Text(
      text = highlight.description!!,
      modifier = Modifier.padding(top = 8.dp),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      overflow = TextOverflow.Ellipsis,
      maxLines = 2,
      minLines = 2,
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}
