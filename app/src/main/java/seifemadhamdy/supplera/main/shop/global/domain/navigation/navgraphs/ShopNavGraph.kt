package seifemadhamdy.supplera.main.shop.global.domain.navigation.navgraphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.shop.detail.presentation.screens.ProductDetailScreen
import seifemadhamdy.supplera.main.shop.global.presentation.screens.ShopScreen
import seifemadhamdy.supplera.main.shop.listing.presentation.screens.ProductListingScreen

@Composable
fun shopNavGraph(
  navHostController: NavHostController,
  contentPadding: PaddingValues,
  appViewModel: AppViewModel = viewModel(),
) =
  navHostController.createGraph(startDestination = AppDestination.Shop.route) {
    composable(route = AppDestination.Shop.route) {
      ShopScreen(navHostController = navHostController, contentPadding = contentPadding)
    }

    composable(route = AppDestination.ProductListing.route) {
      ProductListingScreen(
        navHostController = navHostController,
        contentPadding = contentPadding,
        appViewModel = appViewModel,
      )
    }

    composable(route = AppDestination.ProductDetail.route) {
      ProductDetailScreen(
        navHostController = navHostController,
        contentPadding = contentPadding,
        appViewModel = appViewModel,
      )
    }
  }
