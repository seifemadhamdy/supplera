package seifemadhamdy.supplera.main.basket.checkout.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.api.data.models.Basket
import seifemadhamdy.supplera.api.data.models.Product
import seifemadhamdy.supplera.authentication.join.presentation.components.HighlightingText
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.shop.global.domain.utils.formatAsCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCheckoutScreen(
  navHostController: NavHostController,
  contentPadding: PaddingValues,
  appViewModel: AppViewModel = viewModel(),
) {
  val layoutDirection = LocalLayoutDirection.current
  var currentCustomerAddress: String by rememberSaveable { mutableStateOf("") }
  val shippingAndHandlingPrice = 26
  var currentCustomerFirstName: String? by rememberSaveable { mutableStateOf(null) }
  val context = LocalContext.current
  val basketProducts = remember { mutableStateListOf<Product>() }
  var sumOfBasketProductsPrices by remember { mutableIntStateOf(0) }
  var currentCustomerId by rememberSaveable { mutableIntStateOf(-1) }

  LaunchedEffect(key1 = Unit) {
    appViewModel.getStoredUserEmail(context = context)?.let { userEmail ->
      val currentCustomer = appViewModel.getCustomerByEmail(userEmail)
      currentCustomer?.let { it1 ->
        currentCustomerFirstName = it1.name.substringBefore(" ")
        currentCustomerId = it1.id
        currentCustomerAddress = it1.address

        appViewModel.getLastBasketByCustomerId(it1.id)?.id?.let { basketId ->
          appViewModel.getAllBasketItemsByBasketId(basketId)?.let { basketItems ->
            basketItems.forEach { basketItem ->
              basketItem.productId?.let { productId ->
                appViewModel.getProductById(id = productId)?.let { basketProducts.add(it) }
              }
            }

            sumOfBasketProductsPrices =
              basketProducts.sumOf {
                if (it.discountPercentage!! > 0) {
                  ((it.price!!) * ((100 - it.discountPercentage) / 100f)).toInt()
                } else {
                  it.price!!
                }
              }
          }
        }
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(id = R.string.checkout),
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
          )
        },
        navigationIcon = {
          IconButton(onClick = { navHostController.popBackStack() }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
          }
        },
      )
    }
  ) { currentContentPadding ->
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    var isOrderToBePaidWithCash: Boolean? by remember { mutableStateOf(null) }
    var codConvenienceFees by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(key1 = navHostController) {
      val arguments = navBackStackEntry?.arguments
      isOrderToBePaidWithCash = arguments?.getString("isOrderToBePaidWithCash") == "true"
      codConvenienceFees = if (isOrderToBePaidWithCash == true) 12 else 0
    }

    LazyColumn(
      contentPadding =
        PaddingValues(
          start = currentContentPadding.calculateStartPadding(layoutDirection),
          top = currentContentPadding.calculateTopPadding(),
          end = currentContentPadding.calculateEndPadding(layoutDirection),
          bottom = contentPadding.calculateBottomPadding(),
        )
    ) {
      item {
        Text(
          text =
            stringResource(
              id = R.string.summary_for_customer,
              currentCustomerFirstName ?: stringResource(id = R.string.you),
            ),
          modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
          fontWeight = FontWeight.Medium,
          style = MaterialTheme.typography.titleLarge,
        )
      }

      item {
        OutlinedCard(
          modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = stringResource(id = R.string.items),
              color = MaterialTheme.colorScheme.secondary,
              fontWeight = FontWeight.Medium,
              style = MaterialTheme.typography.titleMedium,
            )

            Text(
              text = sumOfBasketProductsPrices.formatAsCurrency(),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(start = 8.dp),
              style = MaterialTheme.typography.labelLarge,
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = stringResource(id = R.string.shipping_and_handling),
              color = MaterialTheme.colorScheme.secondary,
              fontWeight = FontWeight.Medium,
              style = MaterialTheme.typography.titleMedium,
            )

            Text(
              text = shippingAndHandlingPrice.formatAsCurrency(),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(start = 8.dp),
              style = MaterialTheme.typography.labelLarge,
            )
          }

          if (isOrderToBePaidWithCash == true)
            Row(
              modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = stringResource(id = R.string.cod_convenience_fees),
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleMedium,
              )

              Text(
                text = codConvenienceFees.formatAsCurrency(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.labelLarge,
              )
            }

          HorizontalDivider(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp))

          Row(
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = stringResource(id = R.string.total),
              color = MaterialTheme.colorScheme.secondary,
              fontWeight = FontWeight.Medium,
              style = MaterialTheme.typography.titleMedium,
            )

            Text(
              text =
                (sumOfBasketProductsPrices + shippingAndHandlingPrice + codConvenienceFees)
                  .formatAsCurrency(),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(start = 8.dp),
              style = MaterialTheme.typography.labelLarge,
            )
          }
        }
      }

      item {
        HighlightingText(
          modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
          stringsToHighlight = mutableMapOf(currentCustomerAddress to {}),
          text =
            stringResource(id = R.string.your_order_will_be_delivered_to, currentCustomerAddress),
          highlightSpanStyle = SpanStyle(color = MaterialTheme.colorScheme.tertiary),
          normalSpanStyle = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant),
          style = MaterialTheme.typography.bodyMedium,
        )
      }

      item {
        Button(
          onClick = {
            CoroutineScope(Dispatchers.IO).launch {
              appViewModel.createBasket(Basket(id = -1, customerId = currentCustomerId))

              withContext(Dispatchers.Main) {
                navHostController.popBackStack(AppDestination.Basket.route, false)
              }
            }
          },
          modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
        ) {
          Text(text = stringResource(R.string.place_order))
        }
      }
    }
  }
}
