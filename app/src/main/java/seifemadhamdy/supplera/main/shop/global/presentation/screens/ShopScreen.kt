package seifemadhamdy.supplera.main.shop.global.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.main.shop.global.data.models.Category
import seifemadhamdy.supplera.main.shop.global.data.models.Collection
import seifemadhamdy.supplera.main.shop.global.presentation.components.CollectionCard
import seifemadhamdy.supplera.main.shop.global.presentation.components.ShopSearchBar

@Composable
fun ShopScreen(navHostController: NavHostController, contentPadding: PaddingValues) {
  val layoutDirection = LocalLayoutDirection.current

  val apparel =
    Category(
      headerText = stringResource(id = R.string.apparel),
      collectionList =
        listOf(
          Collection(
            resourceId = R.drawable.academic_regalia,
            text = stringResource(id = R.string.academic_regalia),
          ),
          Collection(
            resourceId = R.drawable.backpacks_and_totes,
            text = stringResource(id = R.string.backpacks_and_totes),
          ),
          Collection(
            resourceId = R.drawable.lab_coats_and_safety_goggles,
            text = stringResource(id = R.string.lab_coats_and_safety_goggles),
          ),
        ),
    )

  val books =
    Category(
      headerText = stringResource(id = R.string.books),
      collectionList =
        listOf(
          Collection(
            resourceId = R.drawable.applied_sciences,
            text = stringResource(id = R.string.applied_sciences),
          ),
          Collection(
            resourceId = R.drawable.formal_sciences,
            text = stringResource(id = R.string.formal_sciences),
          ),
          Collection(
            resourceId = R.drawable.natural_sciences,
            text = stringResource(id = R.string.natural_sciences),
          ),
          Collection(
            resourceId = R.drawable.social_sciences,
            text = stringResource(id = R.string.social_sciences),
          ),
        ),
    )

  val electronics =
    Category(
      headerText = stringResource(id = R.string.electronics),
      collectionList =
        listOf(
          Collection(
            resourceId = R.drawable.adapters_and_cables,
            text = stringResource(id = R.string.adapters_and_cables),
          ),
          Collection(
            resourceId = R.drawable.calculators,
            text = stringResource(id = R.string.calculators),
          ),
          Collection(
            resourceId = R.drawable.chargers_and_batteries,
            text = stringResource(id = R.string.chargers_and_batteries),
          ),
          Collection(
            resourceId = R.drawable.docking_stations_and_usb_hubs,
            text = stringResource(id = R.string.docking_stations_and_usb_hubs),
          ),
          Collection(
            resourceId = R.drawable.ereaders,
            text = stringResource(id = R.string.ereaders),
          ),
          Collection(
            resourceId = R.drawable.external_storage_devices,
            text = stringResource(id = R.string.external_storage_devices),
          ),
          Collection(
            resourceId = R.drawable.graphic_tablets,
            text = stringResource(id = R.string.graphic_tablets),
          ),
          Collection(
            resourceId = R.drawable.headphones_and_earphones,
            text = stringResource(id = R.string.headphones_and_earphones),
          ),
          Collection(resourceId = R.drawable.laptops, text = stringResource(id = R.string.laptops)),
          Collection(resourceId = R.drawable.mice, text = stringResource(id = R.string.mice)),
          Collection(
            resourceId = R.drawable.power_banks,
            text = stringResource(id = R.string.power_banks),
          ),
          Collection(
            resourceId = R.drawable.printers,
            text = stringResource(id = R.string.printers),
          ),
        ),
    )

  val materials =
    Category(
      headerText = stringResource(id = R.string.materials),
      collectionList =
        listOf(
          Collection(resourceId = R.drawable.art, text = stringResource(id = R.string.art)),
          Collection(
            resourceId = R.drawable.engineering,
            text = stringResource(id = R.string.engineering),
          ),
          Collection(
            resourceId = R.drawable.laboratory,
            text = stringResource(id = R.string.laboratory),
          ),
          Collection(
            resourceId = R.drawable.stationery,
            text = stringResource(id = R.string.stationery),
          ),
        ),
    )

  var lazyRowWidth by remember { mutableStateOf(0.dp) }
  val density = LocalDensity.current

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding =
      PaddingValues(
        start = contentPadding.calculateStartPadding(layoutDirection),
        end = contentPadding.calculateEndPadding(layoutDirection),
        bottom =
          contentPadding.calculateBottomPadding() +
            16.dp +
            WindowInsets.ime.asPaddingValues(LocalDensity.current).calculateBottomPadding().run {
              if (this > 0.dp) return@run this - contentPadding.calculateBottomPadding()
              else return@run 0.dp
            },
      ),
  ) {
    item {
      Text(
        text = stringResource(id = R.string.explore_collections),
        modifier =
          Modifier.fillMaxWidth()
            .background(
              brush =
                Brush.verticalGradient(
                  colorStops =
                    arrayOf(
                      0.0f to MaterialTheme.colorScheme.primaryContainer,
                      1.0f to Color.Unspecified,
                    )
                )
            )
            .padding(
              start = 16.dp,
              top = contentPadding.calculateTopPadding() + 16.dp,
              end = 16.dp,
            ),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.displayLarge,
      )
    }

    item {
      ShopSearchBar(
        navHostController = navHostController,
        paddingValues = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp),
      )
    }

    itemsIndexed(
      items = listOf(apparel, books, electronics, materials),
      key = { _, categoryItem -> categoryItem.hashCode() },
    ) { _, category ->
      Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = category.headerText,
          fontWeight = FontWeight.Medium,
          style = MaterialTheme.typography.titleLarge,
        )

        TextButton(
          onClick = {
            navHostController.navigate(
              AppDestination.ProductListing.route.replace("{categoryString}", category.headerText)
            )
          },
          modifier = Modifier.padding(start = 8.dp),
        ) {
          Text(text = stringResource(id = R.string.see_all))
        }
      }

      LazyRow(
        modifier =
          Modifier.fillMaxWidth().padding(top = 16.dp).onGloballyPositioned {
            lazyRowWidth = with(density) { it.size.width.toDp() }
          },
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        itemsIndexed(
          items = category.collectionList,
          key = { _, collectionItem -> collectionItem.hashCode() },
        ) { _, collectionItem ->
          CollectionCard(
            modifier = Modifier.width((lazyRowWidth - 64.dp) / 2f),
            collection = collectionItem,
            navHostController = navHostController,
          )
        }
      }
    }
  }
}
