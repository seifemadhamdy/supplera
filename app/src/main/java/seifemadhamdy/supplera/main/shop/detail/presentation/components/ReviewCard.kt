package seifemadhamdy.supplera.main.shop.detail.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.api.data.models.Review

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewCard(modifier: Modifier = Modifier, review: Review, displayFully: Boolean = false) {
  var showBottomSheet by remember { mutableStateOf(false) }
  var isReviewBodyDisplayedFully by rememberSaveable { mutableStateOf(true) }

  OutlinedCard(
    modifier =
      modifier.clip(CardDefaults.shape).clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = if (isReviewBodyDisplayedFully) null else rememberRipple(),
      ) {
        if (!isReviewBodyDisplayedFully) showBottomSheet = true
      }
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = review.name!!,
        color = MaterialTheme.colorScheme.secondary,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = MaterialTheme.typography.labelLarge,
      )

      Spacer(modifier = Modifier.weight(1f))

      repeat(review.ratingScore!!) {
        Icon(
          imageVector = Icons.Filled.Star,
          contentDescription = null,
          modifier =
            Modifier.padding(start = if (it > 0) 2.dp else if (it == 0) 8.dp else 0.dp).size(18.dp),
          tint = ProgressIndicatorDefaults.linearColor,
        )
      }

      repeat(5 - review.ratingScore) {
        Icon(
          imageVector = Icons.Filled.StarBorder,
          contentDescription = null,
          modifier = Modifier.padding(start = 2.dp).size(18.dp),
          tint = ProgressIndicatorDefaults.linearColor,
        )
      }
    }

    Text(
      text = review.title!!,
      modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
      fontWeight = FontWeight.Medium,
      overflow = TextOverflow.Ellipsis,
      maxLines = if (displayFully) Int.MAX_VALUE else 1,
      style = MaterialTheme.typography.titleMedium,
    )

    Text(
      text = review.body!!,
      modifier =
        Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)
          .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
          .drawWithContent {
            drawContent()

            if (!isReviewBodyDisplayedFully) {
              val contentDrawScopeLineHeight = size.height / 3

              drawRect(
                brush = Brush.horizontalGradient(0f to Color.Black, 1f to Color.Unspecified),
                topLeft = Offset.Zero.copy(y = contentDrawScopeLineHeight * 2),
                size = size.copy(height = contentDrawScopeLineHeight),
                blendMode = BlendMode.DstIn,
              )
            }
          },
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      maxLines = if (displayFully) Int.MAX_VALUE else 3,
      minLines = 3,
      onTextLayout = { isReviewBodyDisplayedFully = !it.hasVisualOverflow },
      style = MaterialTheme.typography.bodyMedium,
    )

    Text(
      text = review.date.toString(),
      modifier = Modifier.padding(all = 16.dp),
      color = MaterialTheme.colorScheme.secondary,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      style = MaterialTheme.typography.labelLarge,
    )
  }

  if (showBottomSheet) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
      onDismissRequest = { showBottomSheet = false },
      modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
      sheetState = sheetState,
      shape = BottomSheetDefaults.HiddenShape,
      dragHandle = {},
      windowInsets =
        BottomSheetDefaults.windowInsets.exclude(
          WindowInsets.statusBars.union(WindowInsets.navigationBars)
        ),
    ) {
      val coroutineScope = rememberCoroutineScope()

      Scaffold(
        topBar = {
          TopAppBar(
            title = {
              Text(
                text = stringResource(id = R.string.ratings_and_reviews),
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
                    showBottomSheet = false
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
          item {
            ReviewCard(
              modifier = Modifier.padding(all = 16.dp),
              review = review,
              displayFully = true,
            )
          }
        }
      }
    }
  }
}
