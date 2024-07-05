package seifemadhamdy.supplera.main.me.sell.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.api.data.models.Sale
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.me.sell.presentation.components.SaleItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviousSubmissionsScreen(
  navHostController: NavHostController,
  contentPadding: PaddingValues,
  appViewModel: AppViewModel = viewModel(),
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(id = R.string.view_previous_submissions),
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
  ) { currentContentPadding ->
    val context = LocalContext.current
    val salesByCurrentCustomer = remember { mutableStateListOf<Sale>() }
    val layoutDirection = LocalLayoutDirection.current

    LaunchedEffect(key1 = Unit) {
      appViewModel.getStoredUserEmail(context = context)?.let { userEmail ->
        appViewModel.getCustomerByEmail(userEmail)?.id?.let { currentCustomerId ->
          appViewModel.getAllSalesByCustomerId(currentCustomerId)?.let {
            salesByCurrentCustomer.addAll(it)
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding =
        PaddingValues(
          start = currentContentPadding.calculateStartPadding(layoutDirection),
          top = currentContentPadding.calculateTopPadding(),
          end = currentContentPadding.calculateEndPadding(layoutDirection),
          bottom = contentPadding.calculateBottomPadding(),
        ),
    ) {
      itemsIndexed(
        items = salesByCurrentCustomer,
        key = { _, saleByCurrentCustomer -> saleByCurrentCustomer.hashCode() },
      ) { _, item ->
        SaleItemCard(
          modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
          sale = item,
        )
      }
    }
  }
}
