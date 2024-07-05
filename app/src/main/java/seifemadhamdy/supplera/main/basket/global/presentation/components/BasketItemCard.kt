package seifemadhamdy.supplera.main.basket.global.presentation.components

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import seifemadhamdy.supplera.api.data.models.BasketItem
import seifemadhamdy.supplera.api.data.models.Photo
import seifemadhamdy.supplera.api.data.models.Product
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.shop.global.domain.utils.formatAsCurrency

@Composable
fun BasketItemCard(
  modifier: Modifier = Modifier,
  basketItem: BasketItem,
  appViewModel: AppViewModel = viewModel(),
) {
  var photo: Photo? by remember { mutableStateOf(null) }
  var product: Product? by remember { mutableStateOf(null) }

  LaunchedEffect(key1 = Unit) {
    basketItem.productId?.let { productId ->
      photo = appViewModel.getAllPhotosByProductId(productId)?.get(0)
      product = appViewModel.getProductById(productId)
    }
  }

  Card(
    modifier = modifier,
    colors =
      CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
      ),
  ) {
    Row(modifier = Modifier.fillMaxWidth().padding(all = 16.dp)) {
      AsyncImage(
        model = photo?.photoUrl,
        contentDescription = null,
        modifier =
          Modifier.fillMaxWidth(0.5f)
            .aspectRatio(1f)
            .clip(shape = CardDefaults.shape)
            .background(color = Color.White),
        contentScale = ContentScale.Fit,
      )

      if (product != null)
        Column(modifier = Modifier.fillMaxHeight().weight(1f).padding(start = 16.dp)) {
          val isDiscounted = (product!!.discountPercentage!!) > 0

          Text(
            text =
              if (isDiscounted) {
                  ((product!!.price!!) * ((100 - product!!.discountPercentage!!) / 100f)).toInt()
                } else {
                  product!!.price!!
                }
                .formatAsCurrency(),
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
          )

          if (isDiscounted)
            Row(
              modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = product!!.price!!.formatAsCurrency(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.LineThrough,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
              )

              Text(
                text = "-${product!!.discountPercentage}%",
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.tertiary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
              )
            }

          Text(
            text = product!!.name!!,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleMedium,
          )

          Text(
            text = product!!.brand!!,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.labelMedium,
          )
        }
    }
  }
}
