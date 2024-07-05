package seifemadhamdy.supplera.main.basket.payment.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.main.basket.payment.presentation.components.BankCardPreviewCard
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderPaymentScreen(navHostController: NavHostController, contentPadding: PaddingValues) {
  val creditOrDebitString = stringResource(id = R.string.credit_or_debit)
  var paymentCardNumber by rememberSaveable { mutableStateOf(value = "") }
  var expirationMonth by rememberSaveable { mutableStateOf(value = "") }
  var expirationYear by rememberSaveable { mutableStateOf(value = "") }
  val hazeState = remember { HazeState() }
  val layoutDirection = LocalLayoutDirection.current
  val paymentMethods = listOf(creditOrDebitString, stringResource(id = R.string.cash))
  var paymentMethodIndex by remember { mutableIntStateOf(0) }
  var cardHolder by rememberSaveable { mutableStateOf(value = "") }
  var cvv by rememberSaveable { mutableStateOf(value = "") }
  val currentDate = LocalDate.now()
  val currentYear = LocalDate.now().year
  var isCardHolderError by rememberSaveable { mutableStateOf(false) }
  var isPaymentCardNumberError by rememberSaveable { mutableStateOf(false) }
  var isExpirationDateError by rememberSaveable { mutableStateOf(false) }
  var isCvvError by rememberSaveable { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(id = R.string.pay_for_order),
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
    LazyColumn(
      modifier = Modifier.fillMaxSize().haze(hazeState),
      contentPadding =
        PaddingValues(
          start = currentContentPadding.calculateStartPadding(layoutDirection),
          top = currentContentPadding.calculateTopPadding(),
          end = currentContentPadding.calculateEndPadding(layoutDirection),
          bottom =
            contentPadding.calculateBottomPadding() +
              WindowInsets.ime.asPaddingValues(LocalDensity.current).calculateBottomPadding().run {
                if (this > 0.dp)
                  return@run this - currentContentPadding.calculateBottomPadding() - 16.dp
                else return@run 0.dp
              } +
              32.dp,
        ),
    ) {
      item {
        MultiChoiceSegmentedButtonRow(
          modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp)
        ) {
          repeat(paymentMethods.size) { paymentMethod ->
            SegmentedButton(
              checked = paymentMethodIndex == paymentMethod,
              onCheckedChange = { paymentMethodIndex = paymentMethod },
              shape =
                SegmentedButtonDefaults.itemShape(
                  index = paymentMethod,
                  count = paymentMethods.size,
                ),
            ) {
              Text(text = paymentMethods[paymentMethod])
            }
          }
        }
      }

      item {
        Column(
          modifier =
            Modifier.fillMaxWidth()
              .padding(start = 16.dp, top = 16.dp, end = 16.dp)
              .animateContentSize()
        ) {
          if (paymentMethods[paymentMethodIndex] == creditOrDebitString) {
            BankCardPreviewCard(
              cardColor = MaterialTheme.colorScheme.tertiary,
              cardNumber = paymentCardNumber,
              cardholder = cardHolder,
              expirationMonth = expirationMonth,
              expirationYear = expirationYear,
              cvv = cvv,
              brand = stringResource(id = R.string.supplera),
            )

            OutlinedTextField(
              value = cardHolder,
              onValueChange = { value ->
                if (isCardHolderError) isCardHolderError = false

                if (value.length == 1) {
                  if (value.all { it.isLetter() }) cardHolder = value
                } else {
                  if (value.all { it.isLetter() || it == '-' || it.isWhitespace() })
                    cardHolder = value
                }
              },
              modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
              placeholder = { Text(text = stringResource(R.string.cardholder)) },
              isError = isCardHolderError,
              keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
              singleLine = true,
            )

            OutlinedTextField(
              value = paymentCardNumber,
              onValueChange = {
                if (isPaymentCardNumberError) isPaymentCardNumberError = false

                if (it.length <= 16 && it.isDigitsOnly())
                  paymentCardNumber = it.filter { input -> !input.isWhitespace() }
              },
              modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
              placeholder = { Text(text = stringResource(R.string.payment_card_number)) },
              isError = isPaymentCardNumberError,
              keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
              singleLine = true,
            )

            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
              OutlinedTextField(
                value = expirationMonth,
                onValueChange = {
                  if (isExpirationDateError) isExpirationDateError = false

                  if (it.length <= 2 && it.isDigitsOnly()) {
                    if (it == "0" || it == "1" || it.isEmpty()) expirationMonth = it

                    if (expirationMonth.isNotEmpty()) {
                      if (expirationMonth == "0" && it.toInt() in 1..9) {
                        expirationMonth = it
                      } else if (expirationMonth == "1" && it.takeLast(1).toInt() in 0..2) {
                        expirationMonth = it
                      }
                    }
                  }
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "MM") },
                isError = isExpirationDateError,
                keyboardOptions =
                  KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true,
              )

              OutlinedTextField(
                value = expirationYear,
                onValueChange = {
                  if (isExpirationDateError) isExpirationDateError = false

                  if (it.length <= 2 && it.isDigitsOnly()) {
                    if (
                      it.length == 1 && it >= currentYear.toString().takeLast(2).dropLast(1) ||
                        expirationYear.isNotEmpty() && it >= currentYear.toString().takeLast(2) ||
                        it.isEmpty()
                    )
                      expirationYear = it
                  }
                },
                modifier = Modifier.padding(start = 8.dp).weight(1f),
                placeholder = { Text(text = "YY") },
                isError = isExpirationDateError,
                keyboardOptions =
                  KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true,
              )

              OutlinedTextField(
                value = cvv,
                onValueChange = {
                  if (isCvvError) isCvvError = false

                  if (it.length <= 3 && it.isDigitsOnly())
                    cvv = it.filter { input -> !input.isWhitespace() }
                },
                modifier = Modifier.padding(start = 8.dp).weight(2f),
                placeholder = { Text(text = stringResource(R.string.cvv)) },
                isError = isCvvError,
                keyboardOptions =
                  KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                singleLine = true,
              )
            }
          } else {
            Text(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              text = stringResource(id = R.string.cod_disclaimer),
              style = MaterialTheme.typography.bodyMedium,
            )
          }
        }
      }

      item {
        Button(
          onClick = {
            if (cardHolder.isEmpty()) isCardHolderError = true
            if (paymentCardNumber.length < 16) isPaymentCardNumberError = true

            val expirationDate: YearMonth? =
              if (expirationMonth.length == 2 && expirationYear.length == 2) {
                YearMonth.parse(
                  "$expirationMonth/$expirationYear",
                  DateTimeFormatterBuilder().appendPattern("MM/yy").toFormatter(Locale.ENGLISH),
                )
              } else {
                null
              }

            if (expirationDate != null) {
              if (expirationDate < YearMonth.from(currentDate)) isExpirationDateError = true
            } else {
              isExpirationDateError = true
            }

            if (cvv.length < 3) isCvvError = true

            if (
              (!isCardHolderError &&
                !isPaymentCardNumberError &&
                !isExpirationDateError &&
                !isCvvError &&
                paymentMethodIndex == 0) || paymentMethodIndex == 1
            ) {
              navHostController.navigate(
                AppDestination.OrderCheckout.route.replace(
                  "{isOrderToBePaidWithCash}",
                  if (paymentMethodIndex == 0) "false" else "true",
                )
              )
            }
          },
          modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
        ) {
          Text(text = stringResource(id = R.string.continue_to_checkout))
        }
      }
    }
  }
}
