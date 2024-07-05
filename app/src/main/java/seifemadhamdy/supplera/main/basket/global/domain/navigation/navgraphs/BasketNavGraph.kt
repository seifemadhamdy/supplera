package seifemadhamdy.supplera.main.basket.global.domain.navigation.navgraphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.basket.checkout.presentation.screens.OrderCheckoutScreen
import seifemadhamdy.supplera.main.basket.global.presentation.screens.BasketScreen
import seifemadhamdy.supplera.main.basket.payment.presentation.screens.OrderPaymentScreen

@Composable
fun basketNavGraph(
  navHostController: NavHostController,
  contentPadding: PaddingValues,
  appViewModel: AppViewModel = viewModel(),
) =
  navHostController.createGraph(startDestination = AppDestination.Basket.route) {
    composable(route = AppDestination.Basket.route) {
      BasketScreen(
        navHostController = navHostController,
        contentPadding = contentPadding,
        appViewModel = appViewModel,
      )
    }

    composable(route = AppDestination.OrderPayment.route) {
      OrderPaymentScreen(navHostController = navHostController, contentPadding = contentPadding)
    }

    composable(route = AppDestination.OrderCheckout.route) {
      OrderCheckoutScreen(
        navHostController = navHostController,
        contentPadding = contentPadding,
        appViewModel = appViewModel,
      )
    }
  }
