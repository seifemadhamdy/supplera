package seifemadhamdy.supplera.main.shop.global.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopSearchBar(
  navHostController: NavHostController,
  paddingValues: PaddingValues = PaddingValues(),
  shouldBeTranslucent: Boolean = false,
) {
  var query by rememberSaveable { mutableStateOf("") }
  val layoutDirection = LocalLayoutDirection.current

  DockedSearchBar(
    query = query,
    onQueryChange = { query = if (it.length == 1) it.trimStart() else it },
    onSearch = {
      navHostController.navigate(
        AppDestination.ProductListing.route.replace("{searchString}", query)
      )

      query = ""
    },
    active = false,
    onActiveChange = {},
    modifier =
      Modifier.fillMaxWidth()
        .padding(
          start = paddingValues.calculateStartPadding(layoutDirection = layoutDirection),
          top = paddingValues.calculateTopPadding(),
          end = paddingValues.calculateEndPadding(layoutDirection = layoutDirection),
          bottom = paddingValues.calculateBottomPadding(),
        ),
    placeholder = { Text(text = stringResource(id = R.string.search_products)) },
    leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
    trailingIcon = {
      Row {
        if (query.isNotEmpty()) {
          IconButton(onClick = { query = "" }) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
          }
        }
      }
    },
    colors =
      if (shouldBeTranslucent)
        SearchBarDefaults.colors(
          containerColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.08f)
        )
      else SearchBarDefaults.colors(),
    content = {},
  )
}
