package seifemadhamdy.supplera.main.shop.listing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import seifemadhamdy.supplera.api.data.models.Photo
import seifemadhamdy.supplera.api.data.models.Product
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.shop.detail.data.models.Rating
import seifemadhamdy.supplera.main.shop.detail.domain.helpers.Rater
import seifemadhamdy.supplera.main.shop.detail.domain.utils.RatingUtils
import seifemadhamdy.supplera.main.shop.global.domain.utils.formatAsCurrency
import java.math.BigDecimal

@Composable
fun ListingCard(
  product: Product,
  appViewModel: AppViewModel = viewModel(),
  navHostController: NavHostController,
) {
  var photo: Photo? by remember { mutableStateOf(null) }
  var ratingScore by remember { mutableStateOf(BigDecimal.valueOf(0.0)) }

  LaunchedEffect(key1 = product) {
    photo = appViewModel.getAllPhotosByProductId(product.id!!)?.get(0)

    ratingScore =
      RatingUtils(
          rating =
            Rating(
              raters =
                mapOf(
                  Rater.FIVE_STAR to appViewModel.getRatersCountWithFiveStarsForProduct(product.id),
                  Rater.FOUR_STAR to appViewModel.getRatersCountWithFourStarsForProduct(product.id),
                  Rater.THREE_STAR to
                    appViewModel.getRatersCountWithThreeStarsForProduct(product.id),
                  Rater.TWO_STAR to appViewModel.getRatersCountWithTwoStarsForProduct(product.id),
                  Rater.ONE_STAR to appViewModel.getRatersCountWithOneStarForProduct(product.id),
                )
            )
        )
        .ratingScore
  }

  Card(
    onClick = {
      navHostController.navigate(
        AppDestination.ProductDetail.route.replace("{productId}", product.id.toString())
      )
    },
    colors =
      CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
      ),
  ) {
    AsyncImage(
      model = photo?.photoUrl,
      contentDescription = null,
      modifier =
        Modifier.fillMaxWidth()
          .padding(start = 16.dp, top = 16.dp, end = 16.dp)
          .aspectRatio(ratio = 1f)
          .clip(shape = CardDefaults.shape)
          .background(color = Color.White),
      contentScale = ContentScale.Fit,
    )

    Row(
      modifier =
        Modifier.height(IntrinsicSize.Min)
          .padding(start = 16.dp, top = 16.dp, end = 16.dp)
          .background(color = MaterialTheme.colorScheme.tertiaryContainer, shape = CircleShape)
          .padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        imageVector = Icons.Filled.Star,
        contentDescription = null,
        modifier = Modifier.size(18.dp),
        tint = MaterialTheme.colorScheme.onTertiaryContainer,
      )

      Text(
        text = ratingScore.toString(),
        modifier = Modifier.padding(start = 4.dp),
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = MaterialTheme.typography.labelLarge,
      )
    }

    val isDiscounted = (product.discountPercentage ?: 0) > 0

    Text(
      text =
        if (isDiscounted) {
            ((product.price ?: 0) * ((100 - product.discountPercentage!!) / 100f)).toInt()
          } else {
            product.price!!
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
          text = product.price!!.formatAsCurrency(),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textDecoration = TextDecoration.LineThrough,
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
          style = MaterialTheme.typography.labelMedium,
        )

        Text(
          text = "-${product.discountPercentage}%",
          modifier = Modifier.padding(start = 8.dp),
          color = MaterialTheme.colorScheme.tertiary,
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
          style = MaterialTheme.typography.labelMedium,
        )
      }

    Text(
      text = product.name!!,
      modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
      color = MaterialTheme.colorScheme.secondary,
      fontWeight = FontWeight.Medium,
      style = MaterialTheme.typography.titleMedium,
    )

    Text(
      text = product.brand!!,
      modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      style = MaterialTheme.typography.labelMedium,
    )
  }
}
