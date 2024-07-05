package seifemadhamdy.supplera.main.shop.detail.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.api.data.models.Basket
import seifemadhamdy.supplera.api.data.models.BasketItem
import seifemadhamdy.supplera.api.data.models.Highlight
import seifemadhamdy.supplera.api.data.models.Photo
import seifemadhamdy.supplera.api.data.models.Product
import seifemadhamdy.supplera.api.data.models.Review
import seifemadhamdy.supplera.databinding.LayoutCarouselBinding
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.global.presentation.components.hazeChildModifier
import seifemadhamdy.supplera.main.shop.detail.data.models.Detail
import seifemadhamdy.supplera.main.shop.detail.data.models.Rating
import seifemadhamdy.supplera.main.shop.detail.domain.adapters.ProductDetailCarouselAdapter
import seifemadhamdy.supplera.main.shop.detail.domain.helpers.Rater
import seifemadhamdy.supplera.main.shop.detail.domain.utils.RatingUtils
import seifemadhamdy.supplera.main.shop.detail.presentation.components.ExpandableText
import seifemadhamdy.supplera.main.shop.detail.presentation.components.HighlightColumn
import seifemadhamdy.supplera.main.shop.detail.presentation.components.ReviewCard
import seifemadhamdy.supplera.main.shop.global.domain.utils.formatAsCurrency
import seifemadhamdy.supplera.main.shop.global.presentation.components.ShopSearchBar
import java.math.BigDecimal

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
  navHostController: NavHostController,
  contentPadding: PaddingValues,
  appViewModel: AppViewModel = viewModel(),
) {
  val hazeState = remember { HazeState() }

  Scaffold(
    topBar = {
      val textStyle = LocalTextStyle.current

      TopAppBar(
        title = {
          ProvideTextStyle(value = textStyle) {
            ShopSearchBar(
              navHostController = navHostController,
              paddingValues = PaddingValues(end = 16.dp),
              shouldBeTranslucent = true,
            )
          }
        },
        modifier =
          Modifier.hazeChildModifier(
            hazeState = hazeState,
            tint =
              MaterialTheme.colorScheme.surfaceColorAtElevation(NavigationBarDefaults.Elevation),
          ),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
      )
    }
  ) { currentContentPadding ->
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    var product: Product? by remember { mutableStateOf(null) }
    var ratingUtils: RatingUtils? by remember { mutableStateOf(null) }
    var ratingScore by remember { mutableStateOf(BigDecimal.ZERO) }
    var photos: List<Photo>? by remember { mutableStateOf(null) }
    var reviews: List<Review>? by remember { mutableStateOf(null) }
    var highlights: List<Highlight>? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    LaunchedEffect(key1 = navBackStackEntry) {
      val arguments = navBackStackEntry?.arguments
      val productId = arguments?.getString("productId")

      productId?.toInt()?.let {
        product = appViewModel.getProductById(it)

        ratingUtils =
          RatingUtils(
            rating =
              Rating(
                raters =
                  mapOf(
                    Rater.FIVE_STAR to appViewModel.getRatersCountWithFiveStarsForProduct(it),
                    Rater.FOUR_STAR to appViewModel.getRatersCountWithFourStarsForProduct(it),
                    Rater.THREE_STAR to appViewModel.getRatersCountWithThreeStarsForProduct(it),
                    Rater.TWO_STAR to appViewModel.getRatersCountWithTwoStarsForProduct(it),
                    Rater.ONE_STAR to appViewModel.getRatersCountWithOneStarForProduct(it),
                  )
              )
          )

        ratingScore = ratingUtils!!.ratingScore
        photos = appViewModel.getAllPhotosByProductId(it)
        reviews = appViewModel.getAllReviewsByProductId(it)
        highlights = appViewModel.getAllHighlightsByProductId(it)
      }
    }

    if (product != null && photos != null && highlights != null && reviews != null) {
      val detail =
        Detail(
          productName = product?.name!!,
          productDescription = product?.description!!,
          photos = photos!!,
          productPrice = product?.price!!,
          discountPercentage = product?.discountPercentage!!,
          highlights = highlights!!,
          reviews = reviews!!,
        )

      val layoutDirection = LocalLayoutDirection.current
      var carouselClickIndex by remember { mutableIntStateOf(0) }
      var showGalleryBottomSheet by remember { mutableStateOf(false) }
      val density = LocalDensity.current
      var highlightsLazyRowWidth by remember { mutableStateOf(0.dp) }
      var reviewsLazyRowWidth by remember { mutableStateOf(0.dp) }
      val lazyListState = rememberLazyListState()

      val isDiscounted = detail.discountPercentage > 0

      LazyColumn(
        modifier = Modifier.fillMaxSize().haze(state = hazeState),
        contentPadding =
          PaddingValues(
            start = contentPadding.calculateStartPadding(layoutDirection),
            end = contentPadding.calculateEndPadding(layoutDirection),
            bottom =
              contentPadding.calculateBottomPadding() +
                16.dp +
                WindowInsets.ime
                  .asPaddingValues(LocalDensity.current)
                  .calculateBottomPadding()
                  .run {
                    if (this > 0.dp) return@run this - contentPadding.calculateBottomPadding()
                    else return@run 0.dp
                  },
          ),
      ) {
        item {
          Text(
            text = detail.productName,
            modifier =
              Modifier.padding(
                start = 16.dp,
                top = currentContentPadding.calculateTopPadding() + 16.dp,
                end = 16.dp,
              ),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineSmall,
          )
        }

        item {
          ExpandableText(
            text = detail.productDescription,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLinesBeforeExpansion = 2,
            style = MaterialTheme.typography.bodyMedium,
          )
        }

        item {
          AndroidViewBinding(
            factory = LayoutCarouselBinding::inflate,
            modifier = Modifier.padding(start = 12.dp, top = 16.dp, end = 12.dp),
          ) {
            carouselRecyclerView.apply {
              if (layoutManager == null) {
                setLayoutManager(
                  CarouselLayoutManager().also {
                    it.carouselAlignment = CarouselLayoutManager.ALIGNMENT_CENTER
                  }
                )

                CarouselSnapHelper().attachToRecyclerView(this)

                adapter =
                  ProductDetailCarouselAdapter().apply {
                    submitList(detail.photos)

                    onItemClick = { position ->
                      carouselClickIndex = position
                      showGalleryBottomSheet = true
                    }
                  }
              }
            }
          }
        }

        item {
          Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text =
                if (isDiscounted) {
                    (detail.productPrice * ((100 - detail.discountPercentage) / 100f)).toInt()
                  } else {
                    detail.productPrice
                  }
                  .formatAsCurrency(),
              color = MaterialTheme.colorScheme.secondary,
              fontWeight = FontWeight.Medium,
              style = MaterialTheme.typography.titleLarge,
            )

            if (isDiscounted)
              Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                  text = detail.productPrice.formatAsCurrency(),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontWeight = FontWeight.Medium,
                  textDecoration = TextDecoration.LineThrough,
                  overflow = TextOverflow.Ellipsis,
                  maxLines = 1,
                  style = MaterialTheme.typography.titleSmall,
                )

                Text(
                  text = "-${detail.discountPercentage}%",
                  modifier = Modifier.padding(start = 8.dp),
                  color = MaterialTheme.colorScheme.tertiary,
                  fontWeight = FontWeight.Medium,
                  overflow = TextOverflow.Ellipsis,
                  maxLines = 1,
                  style = MaterialTheme.typography.titleSmall,
                )
              }
          }
        }

        item {
          Button(
            onClick = {
              CoroutineScope(Dispatchers.IO).launch {
                appViewModel.getStoredUserEmail(context)?.let { userEmail ->
                  val currentCustomer = appViewModel.getCustomerByEmail(email = userEmail)
                  currentCustomer?.id?.let { it1 ->
                    val lastBasketByCurrentCustomer =
                      appViewModel.getLastBasketByCustomerId(customerId = it1)

                    if (lastBasketByCurrentCustomer == null) {
                      val createdBasket =
                        appViewModel.createBasket(basket = Basket(id = -1, customerId = it1))

                      createdBasket?.let {
                        appViewModel.createBasketItem(
                          basketItem =
                            BasketItem(
                              id = -1,
                              basketId = createdBasket.id,
                              productId = product!!.id,
                            )
                        )
                      }
                    } else {
                      appViewModel.createBasketItem(
                        basketItem =
                          BasketItem(
                            id = -1,
                            basketId = lastBasketByCurrentCustomer.id,
                            productId = product!!.id,
                          )
                      )
                    }
                  }
                }
              }
            },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 16.dp),
          ) {
            Text(text = stringResource(id = R.string.add_to_basket))
          }
        }

        item {
          HorizontalDivider(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp))
        }

        item {
          Text(
            text = stringResource(id = R.string.highlights),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleLarge,
          )
        }

        item {
          LazyRow(
            modifier =
              Modifier.fillMaxSize().padding(top = 16.dp).onGloballyPositioned {
                highlightsLazyRowWidth = with(density) { it.size.width.toDp() }
              },
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
          ) {
            items(detail.highlights.size) {
              HighlightColumn(
                modifier = Modifier.width((highlightsLazyRowWidth - 48.dp)),
                highlight = detail.highlights[it],
              )
            }
          }
        }

        item {
          Text(
            text = stringResource(id = R.string.ratings_and_reviews),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleLarge,
          )
        }

        item {
          Text(
            text = stringResource(id = R.string.rating_and_reviews_supporting_text),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
          )
        }

        item {
          Row(
            modifier =
              Modifier.fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Box(
              modifier =
                Modifier.fillMaxHeight()
                  .aspectRatio(1f)
                  .clip(shape = CardDefaults.shape)
                  .background(color = ProgressIndicatorDefaults.linearTrackColor),
              contentAlignment = Alignment.Center,
            ) {
              Text(
                text = ratingScore.toString(),
                modifier = Modifier.padding(all = 16.dp),
                color = ProgressIndicatorDefaults.linearColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge,
              )
            }

            Column(
              modifier = Modifier.fillMaxHeight().padding(start = 8.dp),
              verticalArrangement = Arrangement.SpaceBetween,
              horizontalAlignment = Alignment.End,
            ) {
              repeat(5) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Row(modifier = Modifier.width(98.dp), horizontalArrangement = Arrangement.End) {
                    repeat(5 - it) {
                      Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.padding(start = if (it > 0) 2.dp else 0.dp).size(18.dp),
                        tint = ProgressIndicatorDefaults.linearColor,
                      )
                    }
                  }

                  LinearProgressIndicator(
                    progress = {
                      Rater.fromRatingValue(5 - it)?.let { rater ->
                        ratingUtils?.ratersPercentage(rater)
                      } ?: 0f
                    },
                    modifier = Modifier.weight(1f).padding(start = 4.dp),
                  )
                }
              }

              Text(
                text =
                  "${ratingUtils?.totalRatingsCount} ${if((ratingUtils?.totalRatingsCount?:0) > 1) stringResource(id = R.string.ratings) else stringResource(id = R.string.rating)}",
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
              )
            }
          }
        }

        item {
          LazyRow(
            modifier =
              Modifier.fillMaxWidth().padding(top = 16.dp).onGloballyPositioned {
                reviewsLazyRowWidth = with(density) { it.size.width.toDp() }
              },
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
          ) {
            items(detail.reviews.size) {
              ReviewCard(
                modifier = Modifier.width(reviewsLazyRowWidth - 48.dp),
                review = detail.reviews[it],
              )
            }
          }
        }
      }

      if (showGalleryBottomSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
          onDismissRequest = { showGalleryBottomSheet = false },
          modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
          sheetState = sheetState,
          shape = BottomSheetDefaults.HiddenShape,
          dragHandle = {},
          windowInsets =
            BottomSheetDefaults.windowInsets.exclude(
              WindowInsets.statusBars.union(WindowInsets.navigationBars)
            ),
        ) {
          val pagerState =
            rememberPagerState(initialPage = carouselClickIndex, pageCount = { detail.photos.size })
          val coroutineScope = rememberCoroutineScope()

          Scaffold(
            topBar = {
              TopAppBar(
                title = {
                  Text(
                    text =
                      "${pagerState.targetPage + 1} ${stringResource(id = R.string.out_of)} ${pagerState.pageCount}",
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
                        showGalleryBottomSheet = false
                      }
                    }
                  ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                  }
                },
              )
            }
          ) { contentPadding ->
            var isFirstScrollToItemDone: Boolean by rememberSaveable { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
              val sheetLazyListState = rememberLazyListState()

              LaunchedEffect(key1 = pagerState) {
                snapshotFlow { pagerState.targetPage }
                  .collect {
                    if (!isFirstScrollToItemDone) {
                      sheetLazyListState.scrollToItem(it)
                      isFirstScrollToItemDone = true
                    } else {
                      sheetLazyListState.animateScrollToItem(it)
                    }
                  }
              }

              HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).padding(top = 16.dp),
              ) { page ->
                val zoomState = rememberZoomState(maxScale = 4.0f)

                AsyncImage(
                  model = detail.photos[page].photoUrl,
                  contentDescription = null,
                  modifier =
                    Modifier.fillMaxSize()
                      .clipToBounds()
                      .zoomable(
                        zoomState = zoomState,
                        onDoubleTap = { position ->
                          zoomState.changeScale(
                            when {
                              zoomState.scale < 2f -> 2f
                              zoomState.scale < 4f -> 4f
                              else -> 1f
                            },
                            position,
                          )
                        },
                      ),
                )
              }

              var lazyRowWidth by remember { mutableStateOf(0.dp) }
              val lazyRowCoroutineScope = rememberCoroutineScope()

              LazyRow(
                modifier =
                  Modifier.fillMaxWidth().padding(vertical = 16.dp).onGloballyPositioned {
                    lazyRowWidth = with(density) { it.size.width.toDp() }
                  },
                state = sheetLazyListState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                items(detail.photos.size) {
                  Card(
                    onClick = {
                      if (pagerState.targetPage != it)
                        lazyRowCoroutineScope.launch { pagerState.scrollToPage(it) }
                    },
                    modifier =
                      Modifier.width((lazyRowWidth - 56.dp) / 4f)
                        .aspectRatio(1f)
                        .then(
                          if (pagerState.targetPage == it) Modifier
                          else
                            Modifier.border(
                              border = CardDefaults.outlinedCardBorder(),
                              shape = CircleShape,
                            )
                        ),
                    shape = CircleShape,
                    colors =
                      if (pagerState.targetPage == it)
                        CardDefaults.cardColors(
                          containerColor = ButtonDefaults.buttonColors().containerColor
                        )
                      else CardDefaults.outlinedCardColors(),
                  ) {
                    AsyncImage(
                      model = detail.photos[it].photoUrl,
                      contentDescription = null,
                      modifier =
                        Modifier.fillMaxSize()
                          .padding(all = 4.dp)
                          .clip(CircleShape)
                          .background(color = Color.White),
                      contentScale = ContentScale.Fit,
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
