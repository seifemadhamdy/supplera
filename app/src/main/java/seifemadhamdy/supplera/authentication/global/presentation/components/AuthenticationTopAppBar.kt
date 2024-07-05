package seifemadhamdy.supplera.authentication.global.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationTopAppBar(text: String, navHostController: NavHostController) {
  TopAppBar(
    title = {
      Text(
        text = text,
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
