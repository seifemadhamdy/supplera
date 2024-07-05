package seifemadhamdy.supplera.main.global.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.global.domain.navigation.navgraphs.mainNavGraph
import seifemadhamdy.supplera.main.global.presentation.components.hazeChildModifier

@Composable
fun MainScreen(appViewModel: AppViewModel = viewModel(), appNavHostController: NavHostController) {
  val hazeState = remember { HazeState() }
  val navHostController = rememberNavController()

  Scaffold(
    bottomBar = {
      NavigationBar(
        modifier =
          Modifier.hazeChildModifier(
            hazeState = hazeState,
            tint =
              MaterialTheme.colorScheme.surfaceColorAtElevation(NavigationBarDefaults.Elevation),
          ),
        containerColor = Color.Unspecified,
      ) {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()

        listOf(AppDestination.Shop, AppDestination.Basket, AppDestination.Me).forEach { destination
          ->
          val isCurrentScreenSelected =
            navBackStackEntry?.destination?.hierarchy?.any { it.route == destination.route } == true

          NavigationBarItem(
            icon = {
              when (destination.route) {
                AppDestination.Shop.route ->
                  if (isCurrentScreenSelected) Icons.Filled.ShoppingBag
                  else Icons.Outlined.ShoppingBag
                AppDestination.Basket.route ->
                  if (isCurrentScreenSelected) Icons.Filled.ShoppingBasket
                  else Icons.Outlined.ShoppingBasket
                AppDestination.Me.route ->
                  if (isCurrentScreenSelected) Icons.Filled.Face else Icons.Outlined.Face
                else -> null
              }?.let { Icon(imageVector = it, contentDescription = null) }
            },
            label = {
              when (destination.route) {
                AppDestination.Shop.route -> stringResource(id = R.string.shop)
                AppDestination.Basket.route -> stringResource(id = R.string.basket)
                AppDestination.Me.route -> stringResource(id = R.string.me)
                else -> null
              }?.let { Text(text = it) }
            },
            selected = isCurrentScreenSelected,
            onClick = {
              navHostController.navigate(destination.route) {
                popUpTo(navHostController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
              }
            },
          )
        }
      }
    }
  ) { appPadding ->
    NavHost(
      navController = navHostController,
      graph =
        mainNavGraph(
          navHostController = navHostController,
          appPadding = appPadding,
          appViewModel = appViewModel,
          appNavHostController = appNavHostController,
        ),
      modifier = Modifier.fillMaxSize().haze(hazeState),
    )
  }
}
