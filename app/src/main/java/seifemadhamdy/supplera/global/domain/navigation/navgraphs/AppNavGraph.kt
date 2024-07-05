package seifemadhamdy.supplera.global.domain.navigation.navgraphs

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import seifemadhamdy.supplera.authentication.join.presentation.screens.JoinScreen
import seifemadhamdy.supplera.authentication.signin.presentation.screens.SignInScreen
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.global.presentation.screens.MainScreen
import seifemadhamdy.supplera.onboarding.presentation.screens.OnboardingScreen

@Composable
fun appNavGraph(
  navHostController: NavHostController,
  startDestination: String,
  appViewModel: AppViewModel = viewModel(),
) =
  navHostController.createGraph(startDestination = startDestination) {
    composable(route = AppDestination.Onboarding.route) {
      OnboardingScreen(navController = navHostController)
    }

    composable(route = AppDestination.Join.route) {
      JoinScreen(navHostController = navHostController, appViewModel = appViewModel)
    }
    composable(route = AppDestination.SignIn.route) {
      SignInScreen(navController = navHostController, appViewModel = appViewModel)
    }
    composable(route = AppDestination.Main.route) {
      MainScreen(appViewModel = appViewModel, appNavHostController = navHostController)
    }
  }
