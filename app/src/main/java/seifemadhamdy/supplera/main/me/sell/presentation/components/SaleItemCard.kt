package seifemadhamdy.supplera.main.me.sell.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import seifemadhamdy.supplera.api.data.models.Sale
import seifemadhamdy.supplera.main.me.sell.domain.utils.decodeBitmapFromBase64
import seifemadhamdy.supplera.main.shop.global.domain.utils.formatAsCurrency

@Composable
fun SaleItemCard(modifier: Modifier = Modifier, sale: Sale) {
  Card(
    modifier = modifier,
    colors =
      CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
      ),
  ) {
    Row(modifier = Modifier.fillMaxWidth().padding(all = 16.dp)) {
      AsyncImage(
        model = decodeBitmapFromBase64(sale.photoBase64!!),
        contentDescription = null,
        modifier =
          Modifier.fillMaxWidth(0.5f)
            .aspectRatio(1f)
            .clip(shape = CardDefaults.shape)
            .background(color = Color.White),
        contentScale = ContentScale.Fit,
      )

      Column(modifier = Modifier.fillMaxHeight().weight(1f).padding(start = 16.dp)) {
        Text(
          text = sale.productPrice!!.formatAsCurrency(),
          modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
          style = MaterialTheme.typography.bodyMedium,
        )

        Text(
          text = sale.productName!!,
          modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
          color = MaterialTheme.colorScheme.secondary,
          fontWeight = FontWeight.Medium,
          style = MaterialTheme.typography.titleMedium,
        )

        Text(
          text = sale.productDescription!!,
          modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    }
  }
}
