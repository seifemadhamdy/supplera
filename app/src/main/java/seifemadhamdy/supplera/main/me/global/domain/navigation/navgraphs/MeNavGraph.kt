package seifemadhamdy.supplera.main.me.global.domain.navigation.navgraphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.me.global.presentation.screens.MeScreen
import seifemadhamdy.supplera.main.me.sell.presentation.screens.PreviousSubmissionsScreen
import seifemadhamdy.supplera.main.me.sell.presentation.screens.SellScreen

@Composable
fun meNavGraph(
  navHostController: NavHostController,
  contentPadding: PaddingValues,
  appNavHostController: NavHostController,
  appViewModel: AppViewModel = viewModel(),
) =
  navHostController.createGraph(startDestination = AppDestination.Me.route) {
    composable(route = AppDestination.Me.route) {
      MeScreen(
        navHostController = navHostController,
        contentPadding = contentPadding,
        appNavHostController = appNavHostController,
        appViewModel = appViewModel,
      )
    }

    composable(route = AppDestination.Sell.route) {
      SellScreen(
        navHostController = navHostController,
        contentPadding = contentPadding,
        appViewModel = appViewModel,
      )
    }

    composable(route = AppDestination.PreviousSubmissions.route) {
      PreviousSubmissionsScreen(
        navHostController = navHostController,
        contentPadding = contentPadding,
        appViewModel = appViewModel,
      )
    }
  }
