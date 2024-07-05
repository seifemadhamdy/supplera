package seifemadhamdy.supplera.main.me.global.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MapsHomeWork
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.api.data.models.Customer
import seifemadhamdy.supplera.authentication.global.domain.validators.isEmailValid
import seifemadhamdy.supplera.authentication.join.domain.validators.isNameValid
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MeScreen(
  navHostController: NavHostController,
  contentPadding: PaddingValues,
  appViewModel: AppViewModel = viewModel(),
  appNavHostController: NavHostController,
) {
  var currentCustomer: Customer? by remember { mutableStateOf(null) }
  val context = LocalContext.current

  LaunchedEffect(key1 = Unit) {
    appViewModel.getStoredUserEmail(context)?.let { userEmail ->
      currentCustomer = appViewModel.getCustomerByEmail(email = userEmail)
    }
  }

  val layoutDirection = LocalLayoutDirection.current
  var shouldProfileBeEditable by rememberSaveable { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding =
      PaddingValues(
        start = contentPadding.calculateStartPadding(layoutDirection),
        end = contentPadding.calculateEndPadding(layoutDirection),
        bottom =
          contentPadding.calculateBottomPadding() +
            16.dp +
            WindowInsets.ime.asPaddingValues(LocalDensity.current).calculateBottomPadding().run {
              if (this > 0.dp) return@run this - contentPadding.calculateBottomPadding()
              else return@run 0.dp
            },
      ),
  ) {
    item {
      Text(
        text = stringResource(id = R.string.welcome_back),
        modifier =
          Modifier.fillMaxWidth()
            .background(
              brush =
                Brush.verticalGradient(
                  colorStops =
                    arrayOf(
                      0.0f to MaterialTheme.colorScheme.primaryContainer,
                      1.0f to Color.Unspecified,
                    )
                )
            )
            .padding(
              start = 16.dp,
              top = contentPadding.calculateTopPadding() + 16.dp,
              end = 16.dp,
            ),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.displayLarge,
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Profile",
          fontWeight = FontWeight.Medium,
          style = MaterialTheme.typography.titleLarge,
        )

        TextButton(
          onClick = { shouldProfileBeEditable = !shouldProfileBeEditable },
          modifier = Modifier.padding(start = 8.dp),
        ) {
          Text(text = stringResource(id = R.string.edit))
        }
      }
    }

    item {
      Card(
        modifier =
          Modifier.fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .animateContentSize(),
        colors =
          CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Box(
            modifier =
              Modifier.size(84.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.tertiary),
            contentAlignment = Alignment.Center,
          ) {
            Text(text = "\uD83E\uDDD1\u200D\uD83C\uDF93", fontSize = 40.sp)
          }

          Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
              text = currentCustomer?.name.toString(),
              fontWeight = FontWeight.Medium,
              style = MaterialTheme.typography.titleMedium,
            )

            Text(
              text = currentCustomer?.email.toString(),
              modifier = Modifier.padding(top = 4.dp),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              style = MaterialTheme.typography.labelLarge,
            )
          }
        }

        if (shouldProfileBeEditable) {
          HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

          var name by rememberSaveable { mutableStateOf(currentCustomer?.name ?: "") }
          var isNameError by rememberSaveable { mutableStateOf(false) }
          var email by rememberSaveable { mutableStateOf(currentCustomer?.email ?: "") }
          var isEmailError by rememberSaveable { mutableStateOf(false) }
          var phoneNumber by rememberSaveable { mutableStateOf(currentCustomer?.phone ?: "") }
          var isPhoneNumberError by rememberSaveable { mutableStateOf(false) }
          var address by rememberSaveable { mutableStateOf(currentCustomer?.address ?: "") }
          var isAddressError by rememberSaveable { mutableStateOf(false) }

          OutlinedTextField(
            value = name,
            onValueChange = { value ->
              if (isNameError) isNameError = false

              if (value.length == 1) {
                if (value.all { it.isLetter() }) name = value
              } else {
                if (value.all { it.isLetter() || it == '-' || it.isWhitespace() }) name = value
              }
            },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
            placeholder = { Text(text = stringResource(R.string.name)) },
            leadingIcon = {
              Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null)
            },
            supportingText = {
              Text(
                text = stringResource(R.string.must_be_three_characters_or_more_supporting_text),
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1,
              )
            },
            isError = isNameError,
            keyboardOptions =
              KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            singleLine = true,
          )

          OutlinedTextField(
            value = email,
            onValueChange = {
              if (isEmailError) isEmailError = false
              email = it.filter { input -> !input.isWhitespace() }
            },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp),
            placeholder = { Text(text = stringResource(R.string.email)) },
            leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = null) },
            supportingText = {
              Text(
                text = stringResource(R.string.email_supporting_text),
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1,
              )
            },
            isError = isEmailError,
            keyboardOptions =
              KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            singleLine = true,
          )

          OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
              if (isPhoneNumberError) isPhoneNumberError = false
              if (it.isDigitsOnly() && it.length <= 11) phoneNumber = it
            },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp),
            placeholder = { Text(text = stringResource(R.string.phone_number)) },
            leadingIcon = { Icon(imageVector = Icons.Filled.Phone, contentDescription = null) },
            supportingText = {
              Text(
                text = stringResource(R.string.phone_number_supporting_text),
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1,
              )
            },
            isError = isPhoneNumberError,
            keyboardOptions =
              KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            singleLine = true,
          )

          OutlinedTextField(
            value = address,
            onValueChange = { value ->
              if (isAddressError) isAddressError = false
              if (value.length == 1) {
                if (value.all { !it.isWhitespace() }) address = value
              } else {
                address = value
              }
            },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp),
            placeholder = { Text(text = stringResource(R.string.address)) },
            leadingIcon = {
              Icon(imageVector = Icons.Filled.MapsHomeWork, contentDescription = null)
            },
            supportingText = {
              Text(
                text = stringResource(R.string.must_not_be_empty_supporting_text),
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1,
              )
            },
            isError = isAddressError,
            keyboardOptions =
              KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
          )

          Button(
            onClick = {
              if (!isNameValid(name)) isNameError = true
              if (!isEmailValid(email)) isEmailError = true
              if (phoneNumber.length < 11) isPhoneNumberError = true
              if (address.isEmpty()) isAddressError = true

              if (!isNameError && !isEmailError && !isPhoneNumberError && !isAddressError) {
                if (currentCustomer != null) {
                  val updatedCustomer =
                    Customer(
                      id = currentCustomer!!.id,
                      name = name,
                      email = email,
                      password = currentCustomer!!.password,
                      phone = phoneNumber,
                      address = address,
                    )

                  appViewModel.updateCustomer(updatedCustomer)
                  currentCustomer = updatedCustomer
                  shouldProfileBeEditable = false
                }
              }
            },
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
          ) {
            Text(text = stringResource(R.string.update_profile))
          }
        }
      }
    }

    item {
      Button(
        onClick = {
          CoroutineScope(Dispatchers.IO).launch {
            appViewModel.clearUserEmail(context)

            withContext(Dispatchers.Main) {
              navHostController.popBackStack(
                navHostController.currentDestination!!.id,
                inclusive = true,
              )

              appNavHostController.popBackStack(
                appNavHostController.currentDestination!!.id,
                inclusive = true,
              )

              appNavHostController.navigate(AppDestination.Onboarding.route)
            }
          }
        },
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        colors =
          ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
          ),
      ) {
        Box(modifier = Modifier.weight(1f)) {
          Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null)
        }

        Text(
          text = stringResource(R.string.sign_out_from_supplera),
          modifier = Modifier.padding(start = 8.dp),
        )

        Spacer(modifier = Modifier.weight(1f))
      }
    }

    item { HorizontalDivider(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) }

    item {
      Text(
        text = stringResource(id = R.string.sale),
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.titleLarge,
      )
    }

    item {
      OutlinedCard(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp)
      ) {
        Row(
          modifier =
            Modifier.fillMaxWidth()
              .clickable { navHostController.navigate(AppDestination.Sell.route) }
              .padding(all = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = stringResource(id = R.string.sell_an_item),
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleMedium,
          )

          Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
          )
        }

        HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))

        Row(
          modifier =
            Modifier.fillMaxWidth()
              .clickable { navHostController.navigate(AppDestination.PreviousSubmissions.route) }
              .padding(all = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = stringResource(id = R.string.view_previous_submissions),
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleMedium,
          )

          Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
          )
        }
      }
    }
  }
}
