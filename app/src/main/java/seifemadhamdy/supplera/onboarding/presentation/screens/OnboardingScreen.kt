package seifemadhamdy.supplera.onboarding.presentation.screens

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.global.domain.navigation.destinations.AppDestination
import seifemadhamdy.supplera.onboarding.data.models.Onboarding
import seifemadhamdy.supplera.onboarding.domain.helpers.FirstColorStopColor
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
  isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
  navController: NavController,
) {
  var horizontalPagerPositionInWindow by remember { mutableStateOf(Offset.Zero) }
  var horizontalPagerSize by remember { mutableStateOf(IntSize.Zero) }

  val firstColorStopColor by remember {
    mutableStateOf(
      Animatable(
        if (isSystemInDarkTheme) FirstColorStopColor.SAVVY_CHOICE.darkColor
        else FirstColorStopColor.SAVVY_CHOICE.lightColor
      )
    )
  }

  Column(
    modifier =
      Modifier.fillMaxSize()
        .drawWithContent {
          val rectHeight = horizontalPagerPositionInWindow.y + horizontalPagerSize.height.toFloat()

          drawRect(
            brush =
              Brush.verticalGradient(
                0.0f to firstColorStopColor.value,
                1f to Color.Unspecified,
                startY = 0f,
                endY = rectHeight,
              ),
            size = size.copy(height = rectHeight),
          )

          drawContent()
        }
        .safeDrawingPadding(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    val onboardings =
      listOf(
        Onboarding(
          resourceId =
            R.drawable.casual_life_business_woman_working_on_laptop_while_sitting_on_floor,
          titleId = R.string.savvy_choice,
          descriptionId = R.string.savvy_choice_description,
        ),
        Onboarding(
          resourceId = R.drawable.casual_life_young_people_students,
          titleId = R.string.boosted_ventures,
          descriptionId = R.string.boosted_ventures_description,
        ),
        Onboarding(
          resourceId = R.drawable.casual_life_girl_with_tablet_receiving_drone_delievery,
          titleId = R.string.fast_deliveries,
          descriptionId = R.string.fast_deliveries_description,
        ),
      )

    val pagerState = rememberPagerState(pageCount = { onboardings.size })

    LaunchedEffect(key1 = pagerState, key2 = isSystemInDarkTheme) {
      snapshotFlow { pagerState.currentPage }
        .collect { currentPage ->
          firstColorStopColor.animateTo(
            targetValue =
              when (currentPage) {
                0 ->
                  if (isSystemInDarkTheme) FirstColorStopColor.SAVVY_CHOICE.darkColor
                  else FirstColorStopColor.SAVVY_CHOICE.lightColor
                1 ->
                  if (isSystemInDarkTheme) FirstColorStopColor.BOOSTED_VENTURES.darkColor
                  else FirstColorStopColor.BOOSTED_VENTURES.lightColor
                2 ->
                  if (isSystemInDarkTheme) FirstColorStopColor.FAST_DELIVERIES.darkColor
                  else FirstColorStopColor.FAST_DELIVERIES.lightColor
                else -> Color.Unspecified
              },
            animationSpec = tween(),
          )
        }
    }

    Spacer(modifier = Modifier.weight(1f))

    HorizontalPager(
      state = pagerState,
      modifier =
        Modifier.fillMaxWidth().padding(top = 16.dp).onGloballyPositioned {
          horizontalPagerSize = it.size
          horizontalPagerPositionInWindow = it.positionInWindow()
        },
    ) { page ->
      Column(
        modifier =
          Modifier.graphicsLayer {
            val pageOffset = pagerState.getOffsetFractionForPage(page)

            when {
              pageOffset <= 0 -> {
                scaleX = 1 - abs(pageOffset)
                scaleY = 1 - abs(pageOffset)
              }
              pageOffset <= 1 -> {
                alpha = 1 - abs(pageOffset)
              }
            }
          },
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        AsyncImage(
          model = onboardings[page].resourceId,
          contentDescription = null,
          modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(horizontal = 16.dp),
        )

        Text(
          text = onboardings[page].title,
          modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.SemiBold,
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
          style = MaterialTheme.typography.headlineLarge,
        )

        Text(
          text = onboardings[page].description,
          modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    }

    val coroutineScope = rememberCoroutineScope()

    Row(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
      repeat(times = pagerState.pageCount) {
        Box(
          modifier =
            Modifier.padding(end = if (it != pagerState.pageCount) 8.dp else 0.dp)
              .clip(shape = CircleShape)
              .background(
                if (pagerState.currentPage == it) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
              )
              .size(size = 8.dp)
              .clickable {
                if (pagerState.targetPage != it)
                  coroutineScope.launch { pagerState.scrollToPage(it) }
              }
        )
      }
    }

    Spacer(modifier = Modifier.weight(1f))

    Card(
      modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
      colors =
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
      Button(
        onClick = { navController.navigate(AppDestination.Join.route) },
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
      ) {
        Text(text = stringResource(R.string.get_started))
      }

      FilledTonalButton(
        onClick = { navController.navigate(AppDestination.SignIn.route) },
        modifier =
          Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
      ) {
        Text(text = stringResource(R.string.i_am_already_a_member))
      }
    }
  }
}
