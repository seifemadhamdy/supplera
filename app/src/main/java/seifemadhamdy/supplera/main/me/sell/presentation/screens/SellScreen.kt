package seifemadhamdy.supplera.main.me.sell.presentation.screens

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.api.data.models.Sale
import seifemadhamdy.supplera.authentication.join.domain.validators.isNameValid
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.me.sell.domain.utils.encodeBitmapToBase64

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SellScreen(
  navHostController: NavHostController,
  contentPadding: PaddingValues,
  appViewModel: AppViewModel = viewModel(),
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(id = R.string.sell_an_item_title),
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
    val layoutDirection = LocalLayoutDirection.current
    var name by rememberSaveable { mutableStateOf("") }
    var isNameError by rememberSaveable { mutableStateOf(false) }
    var price by rememberSaveable { mutableStateOf("") }
    var isPriceError by rememberSaveable { mutableStateOf(false) }
    var description by rememberSaveable { mutableStateOf("") }
    var isDescriptionError by rememberSaveable { mutableStateOf(false) }
    var isImageError by rememberSaveable { mutableStateOf(true) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val pickMedia =
      rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri
        ->
        if (uri != null) {
          imageUri = uri
          isImageError = false
        }
      }

    LazyColumn(
      contentPadding =
        PaddingValues(
          start = currentContentPadding.calculateStartPadding(layoutDirection),
          top = currentContentPadding.calculateTopPadding(),
          end = currentContentPadding.calculateEndPadding(layoutDirection),
          bottom =
            contentPadding.calculateBottomPadding() +
              16.dp +
              WindowInsets.ime.asPaddingValues(LocalDensity.current).calculateBottomPadding().run {
                if (this > 0.dp) return@run this - contentPadding.calculateBottomPadding()
                else return@run 0.dp
              },
        )
    ) {
      item {
        Column(
          modifier =
            Modifier.fillMaxWidth()
              .padding(start = 16.dp, top = 16.dp, end = 16.dp)
              .clip(shape = CardDefaults.shape)
              .background(color = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
          Box(
            modifier =
              Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(CardDefaults.shape)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center,
          ) {
            AsyncImage(
              model = imageUri,
              contentDescription = null,
              modifier = Modifier.fillMaxSize().background(color = Color.White),
              onSuccess = {
                CoroutineScope(Dispatchers.Default).launch {
                  bitmap =
                    withContext(Dispatchers.IO) {
                      it.result.drawable.toBitmap().copy(Bitmap.Config.ARGB_8888, false)
                    }
                }
              },
              contentScale = ContentScale.Crop,
            )

            Box(
              modifier =
                Modifier.fillMaxSize()
                  .background(color = MaterialTheme.colorScheme.surface.copy(0.618f))
                  .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                  ) {
                    pickMedia.launch(
                      PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                  },
              contentAlignment = Alignment.Center,
            ) {
              Column(
                modifier = Modifier.padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
              ) {
                Icon(
                  imageVector = Icons.Filled.CloudUpload,
                  contentDescription = null,
                  modifier = Modifier.size(96.dp),
                )

                Text(
                  text = stringResource(id = R.string.upload_image),
                  modifier = Modifier.padding(top = 4.dp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontWeight = FontWeight.SemiBold,
                  style = MaterialTheme.typography.headlineSmall,
                )
              }
            }
          }

          OutlinedTextField(
            value = name,
            onValueChange = { value ->
              if (isNameError) isNameError = false

              if (value.length == 1) {
                if (value.all { !it.isWhitespace() }) name = value
              } else {
                name = value
              }
            },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
            placeholder = { Text(text = stringResource(R.string.name)) },
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
            value = price,
            onValueChange = {
              if (isPriceError) isPriceError = false
              if (it.isDigitsOnly()) price = it
            },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp),
            placeholder = { Text(text = stringResource(R.string.price)) },
            supportingText = {
              Text(
                text = stringResource(R.string.must_not_be_empty_supporting_text),
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1,
              )
            },
            isError = isPriceError,
            keyboardOptions =
              KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            singleLine = true,
          )

          OutlinedTextField(
            value = description,
            onValueChange = { value ->
              if (isDescriptionError) isDescriptionError = false

              if (value.length == 1) {
                if (value.all { !it.isWhitespace() }) description = value
              } else {
                description = value
              }
            },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp),
            placeholder = { Text(text = stringResource(R.string.description)) },
            supportingText = {
              Text(
                text = stringResource(R.string.must_be_three_characters_or_more_supporting_text),
                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1,
              )
            },
            isError = isDescriptionError,
            keyboardOptions =
              KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            singleLine = true,
          )

          Button(
            onClick = {
              if (!isNameValid(name)) isNameError = true
              if (price.isEmpty()) isPriceError = true
              if (!isNameValid(description)) isDescriptionError = true

              if (isImageError)
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.an_image_must_be_uploaded),
                    Toast.LENGTH_SHORT,
                  )
                  .show()

              if (
                !isImageError &&
                  bitmap != null &&
                  !isNameError &&
                  !isPriceError &&
                  !isDescriptionError
              ) {
                CoroutineScope(Dispatchers.IO).launch {
                  appViewModel.getStoredUserEmail(context = context)?.let { userEmail ->
                    appViewModel.getCustomerByEmail(userEmail)?.id?.let { currentCustomerId ->
                      bitmap?.let { bitmap ->
                        appViewModel.createSale(
                          Sale(
                            id = -1,
                            photoBase64 = encodeBitmapToBase64(bitmap = bitmap),
                            productName = name,
                            productDescription = description,
                            productPrice = price.toInt(),
                            customerId = currentCustomerId,
                          )
                        )

                        withContext(Dispatchers.Main) { navHostController.popBackStack() }
                      }
                    }
                  }
                }
              }
            },
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
          ) {
            Text(text = stringResource(R.string.submit_for_review))
          }
        }
      }
    }
  }
}
