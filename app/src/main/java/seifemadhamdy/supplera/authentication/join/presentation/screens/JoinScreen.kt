package seifemadhamdy.supplera.authentication.join.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.api.data.models.Customer
import seifemadhamdy.supplera.authentication.global.domain.validators.isEmailValid
import seifemadhamdy.supplera.authentication.global.domain.validators.isPasswordValid
import seifemadhamdy.supplera.authentication.global.presentation.components.AuthenticationTopAppBar
import seifemadhamdy.supplera.authentication.join.domain.helpers.LegalContent
import seifemadhamdy.supplera.authentication.join.domain.validators.isNameValid
import seifemadhamdy.supplera.authentication.join.presentation.components.HighlightingText
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun JoinScreen(navHostController: NavHostController, appViewModel: AppViewModel = viewModel()) {
  Scaffold(
    topBar = {
      AuthenticationTopAppBar(
        text = stringResource(id = R.string.join_supplera),
        navHostController = navHostController,
      )
    }
  ) { contentPadding ->
    val layoutDirection = LocalLayoutDirection.current
    val termsOfServiceString = stringResource(id = R.string.terms_of_service)
    var legalContentToShow: LegalContent? by remember { mutableStateOf(null) }
    var showLegalBottomSheet by rememberSaveable { mutableStateOf(false) }
    val privacyPolicyString = stringResource(id = R.string.privacy_policy)
    var name by rememberSaveable { mutableStateOf("") }
    var isNameError by rememberSaveable { mutableStateOf(false) }
    var isNameValid by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("") }
    var isEmailError by rememberSaveable { mutableStateOf(false) }
    var isEmailValid by rememberSaveable { mutableStateOf(false) }
    var password by rememberSaveable { mutableStateOf(value = "") }
    var isPasswordError by rememberSaveable { mutableStateOf(value = false) }
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
        HighlightingText(
          modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
          stringsToHighlight =
            mutableMapOf(
              termsOfServiceString to
                {
                  legalContentToShow = LegalContent.TERMS_OF_SERVICE
                  showLegalBottomSheet = true
                },
              privacyPolicyString to
                {
                  legalContentToShow = LegalContent.PRIVACY_POLICY
                  showLegalBottomSheet = true
                },
            ),
          text =
            stringResource(
              id = R.string.account_creation_disclaimer,
              termsOfServiceString,
              privacyPolicyString,
            ),
          highlightSpanStyle = SpanStyle(color = ButtonDefaults.textButtonColors().contentColor),
          normalSpanStyle = SpanStyle(color = MaterialTheme.colorScheme.onSurface),
          style = MaterialTheme.typography.bodyMedium,
        )
      }

      item {
        OutlinedTextField(
          value = name,
          onValueChange = { value ->
            if (isNameError) isNameError = false

            if (value.length == 1) {
              if (value.all { it.isLetter() }) name = value
            } else {
              if (value.all { it.isLetter() || it == '-' || it.isWhitespace() }) name = value
            }

            isNameValid = isNameValid(name)
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
      }

      item {
        OutlinedTextField(
          value = email,
          onValueChange = {
            if (isEmailError) isEmailError = false
            email = it.filter { input -> !input.isWhitespace() }
            isEmailValid = isEmailValid(email)
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
      }

      item {
        OutlinedTextField(
          value = password,
          onValueChange = {
            if (isPasswordError) isPasswordError = false
            password = it
            isPasswordValid = isPasswordValid(password)
          },
          modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp),
          placeholder = { Text(text = stringResource(R.string.password)) },
          leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
          trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
              Icon(
                imageVector =
                  if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                contentDescription = null,
              )
            }
          },
          supportingText = {
            Text(
              text = stringResource(R.string.password_supporting_text),
              modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
              maxLines = 1,
            )
          },
          isError = isPasswordError,
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
              if (!isNameValid) isNameError = true
              if (!isEmailValid) isEmailError = true
              if (!isPasswordValid) isPasswordError = true

              if (!isNameError && !isEmailError && !isPasswordError) {
                CoroutineScope(Dispatchers.IO).launch {
                  appViewModel.createCustomer(
                    customer =
                      Customer(
                        id = -1,
                        name = name,
                        email = email,
                        password = password,
                        phone = "",
                        address = "",
                      )
                  )

                  appViewModel.storeUserEmail(context = context, email = email)

                  withContext(Dispatchers.Main) {
                    navHostController.navigate(AppDestination.Main.route) {
                      popUpTo(route = navHostController.graph.startDestinationRoute!!) {
                        inclusive = true
                      }
                    }
                  }
                }
              }
            },
            modifier = Modifier.weight(1f),
          ) {
            Text(text = stringResource(R.string._continue))
          }

          FilledTonalButton(
            onClick = { navHostController.navigate(AppDestination.SignIn.route) },
            modifier = Modifier.padding(start = 8.dp),
          ) {
            Text(text = stringResource(R.string.i_already_have_an_account))
          }
        }
      }
    }

    if (showLegalBottomSheet) {
      legalContentToShow?.let { legalContent ->
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
          onDismissRequest = { showLegalBottomSheet = false },
          modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
          sheetState = sheetState,
          shape = BottomSheetDefaults.HiddenShape,
          dragHandle = {},
          windowInsets =
            BottomSheetDefaults.windowInsets.exclude(
              WindowInsets.statusBars.union(WindowInsets.navigationBars)
            ),
        ) {
          val isTermsOfServiceLegalContent = legalContent == LegalContent.TERMS_OF_SERVICE
          val coroutineScope = rememberCoroutineScope()

          Scaffold(
            topBar = {
              TopAppBar(
                title = {
                  Text(
                    text =
                      if (isTermsOfServiceLegalContent)
                        stringResource(id = R.string.terms_of_service)
                      else stringResource(id = R.string.privacy_policy),
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                  )
                },
                navigationIcon = {
                  IconButton(
                    onClick = {
                      coroutineScope.launch {
                        sheetState.hide()
                        showLegalBottomSheet = false
                      }
                    }
                  ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                  }
                },
              )
            }
          ) { contentPadding ->
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = contentPadding) {
              val statements =
                if (isTermsOfServiceLegalContent) {
                  mapOf(
                    "Acceptance of Terms" to
                      "By accessing or using Supplera's services, you agree to be bound by these Terms of Service. If you do not agree with any part of these terms, you may not access or use Supplera's services.",
                    "Description of Services" to
                      "Supplera provides an online platform for the purchase of student supplies, including but not limited to textbooks, stationery, technology accessories, and related products. We may also offer additional services, features, or products in the future, which shall also be subject to these Terms of Service.",
                    "Account Registration" to
                      "In order to access certain features of Supplera's services, you may be required to register for an account. You agree to provide accurate, current, and complete information during the registration process and to update such information to keep it accurate, current, and complete. You are responsible for maintaining the confidentiality of your account credentials and for all activities that occur under your account.",
                    "User Conduct" to
                      "You agree to use Supplera's services only for lawful purposes and in accordance with these Terms of Service.",
                    "Intellectual Property" to
                      "All content and materials available on Supplera's website, including but not limited to text, graphics, logos, images, and software, are the intellectual property of Supplera or its licensors and are protected by copyright, trademark, and other intellectual property laws.",
                    "Pricing and Payment" to
                      "Prices for products available on Supplera's website are subject to change without notice. Supplera reserves the right to modify or discontinue any product at any time. Payment for products purchased through Supplera's website must be made using the payment methods provided. By providing payment information, you represent and warrant that you have the legal right to use any payment method(s) provided.",
                    "Shipping and Delivery" to
                      "Supplera will make reasonable efforts to ship products in a timely manner, but we do not guarantee delivery dates. Delivery times may vary depending on the shipping method selected and the destination address. Supplera is not responsible for any delays or damages caused by third-party shipping carriers.",
                    "Returns and Refunds" to
                      "Supplera's return and refund policy is available on our website and is hereby incorporated into these Terms of Service by reference. By purchasing products through Supplera's website, you agree to be bound by our return and refund policy.",
                    "Disclaimer of Warranties" to
                      "Supplera's services are provided on an \"as-is\" and \"as-available\" basis without any warranties of any kind, either express or implied. Supplera disclaims all warranties, including but not limited to warranties of merchantability, fitness for a particular purpose, and non-infringement.",
                    "Limitation of Liability" to
                      "In no event shall Supplera be liable for any direct, indirect, incidental, special, or consequential damages arising out of or in any way connected with the use of or inability to use Supplera's services, whether based on contract, tort, strict liability, or otherwise, even if Supplera has been advised of the possibility of such damages",
                    "Indemnification" to
                      "You agree to indemnify, defend, and hold harmless Supplera and its officers, directors, employees, agents, and affiliates from and against any and all claims, liabilities, damages, losses, costs, expenses, or fees (including reasonable attorneys' fees) arising out of or in any way connected with your use of Supplera's services or your violation of these Terms of Service.",
                    "Governing Law" to
                      "These Terms of Service shall be governed by and construed in accordance with the laws of [Your Jurisdiction], without giving effect to any principles of conflicts of law.",
                    "Changes to Terms of Service" to
                      "Supplera reserves the right to modify or amend these Terms of Service at any time. Any changes will be effective immediately upon posting on Supplera's website. Your continued use of Supplera's services after any such changes constitutes your acceptance of the revised terms.",
                    "Contact Information" to
                      " If you have any questions or concerns about these Terms of Service, please contact us.",
                  )
                } else {
                  mapOf(
                    "Information We Collect" to
                      "We may collect personal information that you provide to us when you register for an account, place an order, or communicate with us. This may include your name, email address, shipping address, payment information, and other information you choose to provide.",
                    "How We Use Your Information" to
                      "We may use your personal information to process and fulfill your orders, communicate with you about your orders, and provide customer support.",
                    "Data Security" to
                      "We take reasonable measures to protect your personal information from unauthorized access, use, or disclosure. However, no method of transmission over the internet or method of electronic storage is 100% secure, so we cannot guarantee absolute security.",
                    "Data Retention" to
                      "We will retain your personal information for as long as necessary to fulfill the purposes for which it was collected, unless a longer retention period is required or permitted by law.",
                    "Children's Privacy" to
                      "Supplera's website and services are not directed to children under the age of 13, and we do not knowingly collect personal information from children under the age of 13. If we become aware that we have collected personal information from a child under the age of 13, we will take steps to delete such information as soon as possible.",
                    "Your Choices" to
                      "You may update or correct your account information at any time by logging into your account settings. You may also unsubscribe from promotional emails and marketing communications by following the instructions in those emails.",
                    "Changes to This Privacy Policy" to
                      "We may update this Privacy Policy from time to time in order to reflect changes to our practices or for other operational, legal, or regulatory reasons. We will notify you of any changes by posting the new Privacy Policy on Supplera's website.",
                    "Contact Us" to
                      "If you have any questions or concerns about this Privacy Policy or our practices regarding your personal information, please contact us.",
                  )
                }

              items(count = statements.size) {
                val statement = statements.entries.toList()[it]

                key(statement.hashCode()) {
                  OutlinedCard(
                    modifier =
                      Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = if (it == statements.size - 1) 16.dp else 0.dp,
                      )
                  ) {
                    Text(
                      text = "${it+1}. ${statement.key}",
                      modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                      fontWeight = FontWeight.Medium,
                      style = MaterialTheme.typography.titleMedium,
                    )

                    Text(
                      text = statement.value,
                      modifier =
                        Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      style = MaterialTheme.typography.bodyMedium,
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
