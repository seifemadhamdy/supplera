package seifemadhamdy.supplera.global.presentation.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import seifemadhamdy.supplera.global.domain.navigation.navgraphs.appNavGraph
import seifemadhamdy.supplera.global.presentation.ui.themes.SuppleraTheme
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel

@Composable
fun AppScreen(
  activity: ComponentActivity,
  startDestination: String,
  appViewModel: AppViewModel = viewModel(),
) {
  val navHostController = rememberNavController()

  BackHandler(
    onBack = {
      CoroutineScope(Dispatchers.Default).launch {
        if (navHostController.previousBackStackEntry == null) {
          activity.finish()
        }
      }
    }
  )

  SuppleraTheme {
    NavHost(
      navController = navHostController,
      graph =
        appNavGraph(
          navHostController = navHostController,
          startDestination = startDestination,
          appViewModel = appViewModel,
        ),
      modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
    )
  }
}
