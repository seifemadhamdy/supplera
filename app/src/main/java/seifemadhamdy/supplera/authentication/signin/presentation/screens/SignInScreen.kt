package seifemadhamdy.supplera.authentication.signin.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.authentication.global.domain.validators.isEmailValid
import seifemadhamdy.supplera.authentication.global.domain.validators.isPasswordValid
import seifemadhamdy.supplera.authentication.global.presentation.components.AuthenticationTopAppBar
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SignInScreen(navController: NavHostController, appViewModel: AppViewModel = viewModel()) {
  Scaffold(
    topBar = {
      AuthenticationTopAppBar(
        text = stringResource(id = R.string.sign_in_to_supplera),
        navHostController = navController,
      )
    }
  ) { contentPadding ->
    val layoutDirection = LocalLayoutDirection.current
    var email by rememberSaveable { mutableStateOf(value = "") }
    var isEmailValid by rememberSaveable { mutableStateOf(value = false) }
    var shouldSuggestPasswordReset by rememberSaveable { mutableStateOf(value = false) }
    var password by rememberSaveable { mutableStateOf(value = "") }
    var isPasswordValid by rememberSaveable { mutableStateOf(value = false) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(value = false) }
    val context = LocalContext.current

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding =
        PaddingValues(
          start = contentPadding.calculateStartPadding(layoutDirection),
          top = contentPadding.calculateTopPadding(),
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
        OutlinedTextField(
          value = email,
          onValueChange = {
            email = it.filter { input -> !input.isWhitespace() }
            isEmailValid = isEmailValid(email)
            if (shouldSuggestPasswordReset) shouldSuggestPasswordReset = false
          },
          modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
          placeholder = { Text(text = stringResource(R.string.email)) },
          leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = null) },
          supportingText = {
            Text(
              text = stringResource(R.string.email_supporting_text),
              modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
              maxLines = 1,
            )
          },
          isError = shouldSuggestPasswordReset,
          keyboardOptions =
            KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
          singleLine = true,
        )
      }

      item {
        OutlinedTextField(
          value = password,
          onValueChange = {
            password = it
            isPasswordValid = isPasswordValid(password)
            if (shouldSuggestPasswordReset) shouldSuggestPasswordReset = false
          },
          modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp),
          placeholder = { Text(text = stringResource(R.string.password)) },
          leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
          supportingText = {
            Text(
              text = stringResource(R.string.sign_in_password_supporting_text),
              modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
              maxLines = 1,
            )
          },
          trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
              Icon(
                imageVector =
                  if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                contentDescription = null,
              )
            }
          },
          isError = shouldSuggestPasswordReset,
          visualTransformation =
            if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          keyboardOptions =
            KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
          singleLine = true,
        )
      }

      item {
        Row(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
          Button(
            onClick = {
              CoroutineScope(Dispatchers.Default).launch {
                if (!shouldSuggestPasswordReset) {
                  withContext(Dispatchers.IO) {
                    val customer = appViewModel.getCustomerByEmail(email = email)

                    withContext(Dispatchers.Default) {
                      if (customer != null && customer.password == password) {
                        withContext(Dispatchers.IO) {
                          appViewModel.storeUserEmail(context = context, email = email)

                          withContext(Dispatchers.Main) {
                            navController.navigate(AppDestination.Main.route) {
                              popUpTo(route = navController.graph.startDestinationRoute!!) {
                                inclusive = true
                              }
                            }
                          }
                        }
                      } else {
                        shouldSuggestPasswordReset = true
                      }
                    }
                  }
                } else {
                  // TODO: Reset password.
                }
              }
            },
            modifier = Modifier.weight(1f),
            enabled = isEmailValid && isPasswordValid,
            colors =
              if (shouldSuggestPasswordReset)
                ButtonDefaults.buttonColors(
                  containerColor = MaterialTheme.colorScheme.errorContainer
                )
              else ButtonDefaults.buttonColors(),
          ) {
            Text(
              text =
                stringResource(
                  if (!shouldSuggestPasswordReset) R.string._continue else R.string.i_forgot
                ),
              color =
                if (shouldSuggestPasswordReset) MaterialTheme.colorScheme.onErrorContainer
                else if (isEmailValid && isPasswordValid) ButtonDefaults.buttonColors().contentColor
                else ButtonDefaults.buttonColors().disabledContentColor,
            )
          }

          FilledTonalButton(
            onClick = { navController.navigate(AppDestination.Join.route) },
            modifier = Modifier.padding(start = 8.dp),
          ) {
            Text(text = stringResource(R.string.i_dont_have_an_account))
          }
        }
      }
    }
  }
}
