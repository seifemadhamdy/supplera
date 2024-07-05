package seifemadhamdy.supplera.global.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.screens.AppScreen
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel

class AppActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    var shouldKeepSplashScreenOnScreen = true
    installSplashScreen().setKeepOnScreenCondition { shouldKeepSplashScreenOnScreen }

    CoroutineScope(Dispatchers.Default).launch {
      val systemBarStyle = SystemBarStyle.light(scrim = 0, darkScrim = 0)

      withContext(Dispatchers.Main) {
        enableEdgeToEdge(statusBarStyle = systemBarStyle, navigationBarStyle = systemBarStyle)
      }

      val appViewModel: AppViewModel by viewModels()

      withContext(Dispatchers.IO) {
        val storedUserEmail = appViewModel.getStoredUserEmail(this@AppActivity)

        withContext(Dispatchers.Default) {
          val startDestination =
            if (storedUserEmail != null) {
              AppDestination.Main.route
            } else {
              AppDestination.Onboarding.route
            }

          withContext(Dispatchers.Main) {
            setContent {
              AppScreen(
                activity = this@AppActivity,
                startDestination = startDestination,
                appViewModel = appViewModel,
              )
            }

            withContext(Dispatchers.Default) { shouldKeepSplashScreenOnScreen = false }
          }
        }
      }
    }
  }
}
