package seifemadhamdy.supplera.main.global.domain.navigation.navgraphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.basket.global.domain.navigation.navgraphs.basketNavGraph
import seifemadhamdy.supplera.main.me.global.domain.navigation.navgraphs.meNavGraph
import seifemadhamdy.supplera.main.shop.global.domain.navigation.navgraphs.shopNavGraph

@Composable
fun mainNavGraph(
  navHostController: NavHostController,
  appPadding: PaddingValues,
  appViewModel: AppViewModel = viewModel(),
  appNavHostController: NavHostController,
) =
  navHostController.createGraph(startDestination = AppDestination.Shop.route) {
    composable(AppDestination.Shop.route) {
      val shopNavHostController = rememberNavController()
      val hazeState = remember { HazeState() }

      NavHost(
        navController = shopNavHostController,
        graph =
          shopNavGraph(
            navHostController = shopNavHostController,
            contentPadding = appPadding,
            appViewModel = appViewModel,
          ),
        modifier = Modifier.haze(hazeState),
      )
    }

    composable(AppDestination.Basket.route) {
      val basketNavHostController = rememberNavController()

      NavHost(
        navController = basketNavHostController,
        graph =
          basketNavGraph(
            navHostController = basketNavHostController,
            contentPadding = appPadding,
            appViewModel = appViewModel,
          ),
      )
    }

    composable(AppDestination.Me.route) {
      val meNavHostController = rememberNavController()

      NavHost(
        navController = meNavHostController,
        graph =
          meNavGraph(
            navHostController = meNavHostController,
            contentPadding = appPadding,
            appNavHostController = appNavHostController,
            appViewModel = appViewModel,
          ),
      )
    }
  }
