package seifemadhamdy.supplera.main.shop.global.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onSurface
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_dark_onSurfaceVariant
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onSurface
import seifemadhamdy.supplera.global.presentation.ui.colors.md_theme_light_onSurfaceVariant
import seifemadhamdy.supplera.main.shop.global.data.models.Collection

@Composable
fun CollectionCard(
  modifier: Modifier = Modifier,
  collection: Collection,
  navHostController: NavHostController,
) {
  var containerColor by rememberSaveable { mutableStateOf(collection.containerColor) }

  containerColor =
    if (containerColor != null) containerColor
    else CardDefaults.cardColors().containerColor.toArgb()

  var textColor by rememberSaveable { mutableStateOf(collection.textColor) }

  textColor =
    if (textColor != null) textColor
    else if (containerColor == CardDefaults.cardColors().containerColor.toArgb())
      CardDefaults.cardColors().contentColor.toArgb()
    else if (
      ColorUtils.calculateContrast(md_theme_dark_onSurfaceVariant.toArgb(), containerColor!!) >
        ColorUtils.calculateContrast(md_theme_light_onSurfaceVariant.toArgb(), containerColor!!)
    )
      md_theme_dark_onSurface.toArgb()
    else md_theme_light_onSurface.toArgb()

  Card(
    onClick = {
      navHostController.navigate(
        AppDestination.ProductListing.route.replace("{collectionString}", collection.text)
      )
    },
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = Color(containerColor!!)),
  ) {
    Column(
      modifier =
        Modifier.fillMaxSize()
          .background(
            brush =
              Brush.linearGradient(
                colorStops =
                  arrayOf(
                    0.0f to Color.Unspecified,
                    1.0f to MaterialTheme.colorScheme.surfaceContainerHigh,
                  )
              )
          )
          .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
      Text(
        text = collection.text,
        color = Color(textColor!!),
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        minLines = 2,
        style = MaterialTheme.typography.bodyMedium,
      )

      val isSystemInDarkTheme = isSystemInDarkTheme()

      AsyncImage(
        model = collection.resourceId,
        contentDescription = collection.contentDescription,
        modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(top = 8.dp),
        onSuccess = {
          CoroutineScope(Dispatchers.Default).launch {
            if (collection.picassoMode) {
              val bitmap =
                withContext(Dispatchers.IO) {
                  it.result.drawable.toBitmap().copy(Bitmap.Config.ARGB_8888, false)
                }

              Palette.from(bitmap).generate { palette ->
                val swatch =
                  if (isSystemInDarkTheme) {
                    palette?.darkVibrantSwatch ?: palette?.darkMutedSwatch
                  } else {
                    palette?.lightVibrantSwatch ?: palette?.lightMutedSwatch
                  }

                containerColor = swatch?.rgb
                textColor = swatch?.titleTextColor

                CoroutineScope(Dispatchers.IO).launch { bitmap.recycle() }
              }
            }
          }
        },
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopCenter,
      )
    }
  }
}
