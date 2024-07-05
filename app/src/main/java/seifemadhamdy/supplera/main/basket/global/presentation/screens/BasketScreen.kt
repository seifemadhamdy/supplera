package seifemadhamdy.supplera.main.basket.global.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.api.data.models.BasketItem
import seifemadhamdy.supplera.api.data.models.Product
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.basket.global.presentation.components.BasketItemCard
import seifemadhamdy.supplera.main.global.presentation.components.hazeChildModifier
import seifemadhamdy.supplera.main.shop.global.domain.utils.formatAsCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketScreen(
  navHostController: NavHostController,
  contentPadding: PaddingValues,
  appViewModel: AppViewModel = viewModel(),
) {
  val context = LocalContext.current
  val basketItems = remember { mutableStateListOf<BasketItem>() }
  var currentCustomerFirstName: String? by rememberSaveable { mutableStateOf(null) }
  val basketProducts = remember { mutableStateListOf<Product>() }

  LaunchedEffect(key1 = Unit) {
    appViewModel.getStoredUserEmail(context = context)?.let { userEmail ->
      val currentCustomer = appViewModel.getCustomerByEmail(userEmail)
      currentCustomer?.let { it1 ->
        currentCustomerFirstName = it1.name.substringBefore(" ")

        appViewModel.getLastBasketByCustomerId(customerId = it1.id)?.id?.let { it2 ->
          val allBasketItemsByBasketId = appViewModel.getAllBasketItemsByBasketId(basketId = it2)
          allBasketItemsByBasketId?.let { it3 ->
            it3.forEach {
              it.productId?.let { it1 ->
                appViewModel.getProductById(id = it1)?.let { it4 -> basketProducts.add(it4) }
              }
            }

            basketItems.addAll(it3)
          }
        }
      }
    }
  }

  var paymentColumnHeight by remember { mutableStateOf(0.dp) }

  Box(modifier = Modifier.fillMaxSize()) {
    val hazeState = remember { HazeState() }
    val layoutDirection = LocalLayoutDirection.current

    LazyColumn(
      modifier = Modifier.fillMaxSize().haze(hazeState),
      contentPadding =
        PaddingValues(
          start = contentPadding.calculateStartPadding(layoutDirection),
          end = contentPadding.calculateEndPadding(layoutDirection),
          bottom = contentPadding.calculateBottomPadding() + paymentColumnHeight + 32.dp,
        ),
    ) {
      item {
        Text(
          text = "$currentCustomerFirstName's\n${stringResource(id = R.string.basket)}",
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
        Text(
          text =
            when {
              basketItems.size > 1 -> {
                "${basketItems.size} ${stringResource(id = R.string.items)}"
              }
              basketItems.size == 1 -> {
                "${basketItems.size} ${stringResource(id = R.string.item)}"
              }
              else -> {
                stringResource(id = R.string.no_item_has_been_added_yet)
              }
            },
          modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
          fontWeight = FontWeight.Medium,
          style = MaterialTheme.typography.titleLarge,
        )
      }

      itemsIndexed(items = basketItems, key = { _, basketItem -> basketItem.hashCode() }) { _, item
        ->
        var isRemoved by rememberSaveable { mutableStateOf(false) }

        val swipeToDismissBoxState =
          rememberSwipeToDismissBoxState(
            confirmValueChange = { swipeToDismissBoxValue ->
              if (swipeToDismissBoxValue == SwipeToDismissBoxValue.EndToStart) {
                item.id?.let { it1 -> appViewModel.deleteBasketItemById(it1) }
                basketProducts.remove(basketProducts.find { it.id == item.productId })
                basketItems.remove(item)
                isRemoved = true
                return@rememberSwipeToDismissBoxState true
              }

              false
            }
          )

        AnimatedVisibility(
          visible = !isRemoved,
          exit = shrinkOut(shrinkTowards = Alignment.CenterStart) + fadeOut(),
        ) {
          SwipeToDismissBox(
            state = swipeToDismissBoxState,
            backgroundContent = {
              if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart)
                Box(
                  modifier =
                    Modifier.fillMaxSize()
                      .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                      .clip(CardDefaults.shape)
                      .background(color = MaterialTheme.colorScheme.errorContainer)
                ) {
                  Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    modifier = Modifier.padding(all = 16.dp).align(Alignment.CenterEnd),
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                  )
                }
            },
          ) {
            BasketItemCard(
              modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
              basketItem = item,
              appViewModel = appViewModel,
            )
          }
        }
      }
    }

    val density = LocalDensity.current

    if (basketItems.isNotEmpty())
      Column(
        modifier =
          Modifier.align(Alignment.BottomStart)
            .fillMaxWidth()
            .padding(
              PaddingValues(
                start = contentPadding.calculateStartPadding(layoutDirection) + 16.dp,
                top = contentPadding.calculateTopPadding() + 16.dp,
                end = contentPadding.calculateEndPadding(layoutDirection) + 16.dp,
                bottom = contentPadding.calculateBottomPadding() + 16.dp,
              )
            )
            .onGloballyPositioned { paymentColumnHeight = with(density) { it.size.height.toDp() } }
            .hazeChildModifier(
              hazeState = hazeState,
              shape = CardDefaults.shape,
              tint =
                MaterialTheme.colorScheme.surfaceColorAtElevation(NavigationBarDefaults.Elevation),
            )
      ) {
        Row(
          modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = stringResource(id = R.string.subtotal),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineSmall,
          )

          Text(
            text =
              basketProducts
                .sumOf {
                  if (it.discountPercentage!! > 0) {
                    ((it.price!!) * ((100 - it.discountPercentage) / 100f)).toInt()
                  } else {
                    it.price!!
                  }
                }
                .formatAsCurrency(),
            modifier = Modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleLarge,
          )
        }

        Button(
          onClick = { navHostController.navigate(AppDestination.OrderPayment.route) },
          modifier =
            Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
        ) {
          Text(text = stringResource(id = R.string.continue_to_payment))
        }
      }
  }
}
