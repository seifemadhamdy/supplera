package seifemadhamdy.supplera.main.shop.listing.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.api.data.models.Product
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.global.presentation.components.hazeChildModifier
import seifemadhamdy.supplera.main.shop.global.domain.utils.formatAsCurrency
import seifemadhamdy.supplera.main.shop.global.presentation.components.ShopSearchBar
import seifemadhamdy.supplera.main.shop.listing.domain.helpers.Condition
import seifemadhamdy.supplera.main.shop.listing.domain.helpers.SortBy
import seifemadhamdy.supplera.main.shop.listing.domain.utils.filterAllProductsBy
import seifemadhamdy.supplera.main.shop.listing.presentation.components.ListingCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProductListingScreen(
  navHostController: NavHostController,
  contentPadding: PaddingValues,
  appViewModel: AppViewModel = viewModel(),
) {
  val hazeState = remember { HazeState() }
  val productsByCriteria = remember { mutableStateListOf<Product>() }
  var priceSliderPosition by remember { mutableStateOf(1.toFloat()..Int.MAX_VALUE.toFloat()) }
  val brands = remember { mutableStateListOf<String>() }
  val allString = stringResource(id = R.string.all)
  val brandsSelectionStates = remember { mutableStateListOf<Boolean>() }

  val navBackStackEntry by navHostController.currentBackStackEntryAsState()

  val categories =
    listOf(
      allString,
      stringResource(id = R.string.apparel),
      stringResource(id = R.string.books),
      stringResource(id = R.string.electronics),
      stringResource(id = R.string.materials),
    )

  val categoriesSelectionStates = remember { mutableStateListOf<Boolean>() }
  val collections = remember { mutableStateListOf<String>() }
  val collectionsSelectionStates = remember { mutableStateListOf<Boolean>() }
  val resources = LocalContext.current.resources

  LaunchedEffect(key1 = navHostController) {
    val arguments = navBackStackEntry?.arguments
    val searchString = arguments?.getString("searchString")
    val categoryString = arguments?.getString("categoryString")
    val collectionString = arguments?.getString("collectionString")

    if (searchString != "{searchString}") {
      filterAllProductsBy(appViewModel = appViewModel)
        ?.filter {
          searchString?.let { it1 ->
            it.name?.contains(it1, true) == true || it.brand?.contains(it1, true) == true
          } == true
        }
        ?.let { products ->
          productsByCriteria.addAll(products)

          priceSliderPosition =
            (productsByCriteria.minOfOrNull {
                (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
              } ?: 1)
              .toFloat()..(productsByCriteria.maxOfOrNull {
                  (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
                } ?: Int.MAX_VALUE)
                .toFloat()

          categoriesSelectionStates.apply {
            repeat(categories.size) { if (categories[it] != allString) add(false) else add(true) }
          }

          if (brands.isNotEmpty()) brands.clear()
          brands.addAll(productsByCriteria.map { it.brand!! }.distinct())
          brandsSelectionStates.apply { repeat(brands.size) { add(false) } }
        }
    } else if (categoryString != "{categoryString}") {
      filterAllProductsBy(appViewModel = appViewModel, category = categoryString)?.let { products ->
        productsByCriteria.addAll(products)

        priceSliderPosition =
          (productsByCriteria.minOfOrNull {
              (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
            } ?: 1)
            .toFloat()..(productsByCriteria.maxOfOrNull {
                (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
              } ?: Int.MAX_VALUE)
              .toFloat()

        categoriesSelectionStates.apply {
          repeat(categories.size) {
            if (categories[it] != categoryString) add(false) else add(true)
          }
        }

        when (categoryString) {
          resources.getString(R.string.apparel) -> {
            collections.clear()
            collectionsSelectionStates.clear()
            collections.addAll(
              listOf(
                resources.getString(R.string.all),
                resources.getString(R.string.academic_regalia),
                resources.getString(R.string.backpacks_and_totes),
                resources.getString(R.string.lab_coats_and_safety_goggles),
              )
            )
          }
          resources.getString(R.string.books) -> {
            collections.clear()
            collectionsSelectionStates.clear()
            collections.addAll(
              listOf(
                resources.getString(R.string.all),
                resources.getString(R.string.applied_sciences),
                resources.getString(R.string.formal_sciences),
                resources.getString(R.string.natural_sciences),
                resources.getString(R.string.social_sciences),
              )
            )
          }
          resources.getString(R.string.electronics) -> {
            collections.clear()
            collectionsSelectionStates.clear()
            collections.addAll(
              listOf(
                resources.getString(R.string.all),
                resources.getString(R.string.adapters_and_cables),
                resources.getString(R.string.calculators),
                resources.getString(R.string.chargers_and_batteries),
                resources.getString(R.string.docking_stations_and_usb_hubs),
                resources.getString(R.string.ereaders),
                resources.getString(R.string.external_storage_devices),
                resources.getString(R.string.graphic_tablets),
                resources.getString(R.string.headphones_and_earphones),
                resources.getString(R.string.laptops),
                resources.getString(R.string.mice),
                resources.getString(R.string.power_banks),
                resources.getString(R.string.printers),
              )
            )
          }
          resources.getString(R.string.materials) -> {
            collections.clear()
            collectionsSelectionStates.clear()
            collections.addAll(
              listOf(
                resources.getString(R.string.all),
                resources.getString(R.string.art),
                resources.getString(R.string.engineering),
                resources.getString(R.string.laboratory),
                resources.getString(R.string.stationery),
              )
            )
          }
        }

        collectionsSelectionStates.apply {
          repeat(collections.size) { if (it != 0) add(false) else add(true) }
        }

        if (brands.isNotEmpty()) brands.clear()
        brands.addAll(productsByCriteria.map { it.brand!! }.distinct())
        brandsSelectionStates.apply { repeat(brands.size) { add(false) } }
      }
    } else if (collectionString != "{collectionString}") {
      filterAllProductsBy(appViewModel = appViewModel, collection = collectionString)?.let {
        products ->
        productsByCriteria.addAll(products)

        priceSliderPosition =
          (productsByCriteria.minOfOrNull {
              (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
            } ?: 1)
            .toFloat()..(productsByCriteria.maxOfOrNull {
                (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
              } ?: Int.MAX_VALUE)
              .toFloat()

        appViewModel
          .getAllCollections()
          ?.find { it.name == collectionString }
          ?.let { collection ->
            appViewModel
              .getAllCategories()
              ?.find { it.id == collection.categoryId }
              ?.let { category ->
                categoriesSelectionStates.apply {
                  repeat(categories.size) {
                    if (categories[it] != category.name) add(false) else add(true)
                  }
                }

                when (category.name) {
                  resources.getString(R.string.apparel) -> {
                    collections.clear()
                    collectionsSelectionStates.clear()
                    collections.addAll(
                      listOf(
                        resources.getString(R.string.all),
                        resources.getString(R.string.academic_regalia),
                        resources.getString(R.string.backpacks_and_totes),
                        resources.getString(R.string.lab_coats_and_safety_goggles),
                      )
                    )
                  }
                  resources.getString(R.string.books) -> {
                    collections.clear()
                    collectionsSelectionStates.clear()
                    collections.addAll(
                      listOf(
                        resources.getString(R.string.all),
                        resources.getString(R.string.applied_sciences),
                        resources.getString(R.string.formal_sciences),
                        resources.getString(R.string.natural_sciences),
                        resources.getString(R.string.social_sciences),
                      )
                    )
                  }
                  resources.getString(R.string.electronics) -> {
                    collections.clear()
                    collectionsSelectionStates.clear()
                    collections.addAll(
                      listOf(
                        resources.getString(R.string.all),
                        resources.getString(R.string.adapters_and_cables),
                        resources.getString(R.string.calculators),
                        resources.getString(R.string.chargers_and_batteries),
                        resources.getString(R.string.docking_stations_and_usb_hubs),
                        resources.getString(R.string.ereaders),
                        resources.getString(R.string.external_storage_devices),
                        resources.getString(R.string.graphic_tablets),
                        resources.getString(R.string.headphones_and_earphones),
                        resources.getString(R.string.laptops),
                        resources.getString(R.string.mice),
                        resources.getString(R.string.power_banks),
                        resources.getString(R.string.printers),
                      )
                    )
                  }
                  resources.getString(R.string.materials) -> {
                    collections.clear()
                    collectionsSelectionStates.clear()
                    collections.addAll(
                      listOf(
                        resources.getString(R.string.all),
                        resources.getString(R.string.art),
                        resources.getString(R.string.engineering),
                        resources.getString(R.string.laboratory),
                        resources.getString(R.string.stationery),
                      )
                    )
                  }
                }

                collectionsSelectionStates.apply {
                  repeat(collections.size) {
                    if (collections[it] != collectionString) add(false) else add(true)
                  }
                }
              }
          }

        if (brands.isNotEmpty()) brands.clear()
        brands.addAll(productsByCriteria.map { it.brand!! }.distinct())
        brandsSelectionStates.apply { repeat(brands.size) { add(false) } }
      }
    }
  }

  Scaffold(
    topBar = {
      val textStyle = LocalTextStyle.current
      var showFiltersBottomSheet by rememberSaveable { mutableStateOf(false) }

      TopAppBar(
        title = {
          ProvideTextStyle(value = textStyle) {
            ShopSearchBar(
              navHostController = navHostController,
              paddingValues = PaddingValues(end = 4.dp),
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
        actions = {
          IconButton(onClick = { showFiltersBottomSheet = true }) {
            Icon(imageVector = Icons.Filled.Tune, contentDescription = null)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
      )

      val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
      val coroutineScope = rememberCoroutineScope()
      val arrivalDateString = stringResource(id = R.string.arrival_date)
      val higherPriceString = stringResource(id = R.string.higher_price)
      val lowerPriceString = stringResource(id = R.string.lower_price)
      val popularityString = stringResource(id = R.string.popularity)
      val ratingString = stringResource(id = R.string.rating)

      val sorts =
        listOf(
          arrivalDateString,
          higherPriceString,
          lowerPriceString,
          popularityString,
          ratingString,
        )

      val sortsSelectionStates = remember {
        mutableStateListOf<Boolean>().apply {
          repeat(sorts.size) { if (sorts[it] != popularityString) add(false) else add(true) }
        }
      }

      var isPriceSwitchChecked by remember { mutableStateOf(false) }
      val newString = stringResource(id = R.string._new)
      val usedString = stringResource(id = R.string.used)
      val conditions = listOf(allString, newString, usedString)
      var checkedConditionIndex by remember { mutableIntStateOf(0) }
      var isBrandSwitchChecked by remember { mutableStateOf(false) }
      var ratingSliderPosition by remember { mutableFloatStateOf(1f) }
      var isRatingSwitchChecked by remember { mutableStateOf(false) }

      if (showFiltersBottomSheet) {
        ModalBottomSheet(
          onDismissRequest = {
            CoroutineScope(Dispatchers.IO).launch {
              filterAllProductsBy(
                  appViewModel = appViewModel,
                  sortBy =
                    when (sorts[sortsSelectionStates.indexOf(true)]) {
                      arrivalDateString -> SortBy.ARRIVAL_DATE
                      higherPriceString -> SortBy.HIGHER_PRICE
                      lowerPriceString -> SortBy.LOWER_PRICE
                      ratingString -> SortBy.RATING
                      else -> SortBy.POPULARITY
                    },
                  startingPrice =
                    if (isPriceSwitchChecked) priceSliderPosition.start.toInt() else 1,
                  endingPrice =
                    if (isPriceSwitchChecked) priceSliderPosition.endInclusive.toInt()
                    else Int.MAX_VALUE,
                  condition =
                    when (conditions[checkedConditionIndex]) {
                      newString -> Condition.NEW
                      usedString -> Condition.USED
                      else -> {
                        Condition.ANY
                      }
                    },
                  category =
                    if (categories[categoriesSelectionStates.indexOf(true)] != allString)
                      categories[categoriesSelectionStates.indexOf(true)]
                    else null,
                  collection =
                    if (
                      collections.isNotEmpty() &&
                        collections[collectionsSelectionStates.indexOf(true)] != allString
                    )
                      collections[collectionsSelectionStates.indexOf(true)]
                    else null,
                  brands =
                    if (
                      isBrandSwitchChecked &&
                        brands[brandsSelectionStates.indexOf(true)] != allString
                    )
                      brands.filterIndexed { index, _ -> brandsSelectionStates[index] }
                    else null,
                  rating = if (isRatingSwitchChecked) ratingSliderPosition.toInt() else null,
                )
                ?.let { products ->
                  productsByCriteria.clear()
                  productsByCriteria.addAll(products)

                  priceSliderPosition =
                    (productsByCriteria.minOfOrNull {
                        (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
                      } ?: 1)
                      .toFloat()..(productsByCriteria.maxOfOrNull {
                          (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
                        } ?: Int.MAX_VALUE)
                        .toFloat()

                  if (brands.isNotEmpty()) brands.clear()
                  brands.addAll(productsByCriteria.map { it.brand!! }.distinct())
                  brandsSelectionStates.apply { repeat(brands.size) { add(false) } }
                }

              withContext(Dispatchers.Default) { showFiltersBottomSheet = false }
            }
          },
          modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection()),
          sheetState = sheetState,
          shape = BottomSheetDefaults.HiddenShape,
          dragHandle = {},
          windowInsets =
            BottomSheetDefaults.windowInsets.exclude(
              WindowInsets.statusBars.union(WindowInsets.navigationBars)
            ),
        ) {
          Scaffold(
            topBar = {
              TopAppBar(
                title = {
                  Text(
                    text = stringResource(id = R.string.filters),
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                  )
                },
                navigationIcon = {
                  IconButton(
                    onClick = {
                      CoroutineScope(Dispatchers.IO).launch {
                        filterAllProductsBy(
                            appViewModel = appViewModel,
                            sortBy =
                              when (sorts[sortsSelectionStates.indexOf(true)]) {
                                arrivalDateString -> SortBy.ARRIVAL_DATE
                                higherPriceString -> SortBy.HIGHER_PRICE
                                lowerPriceString -> SortBy.LOWER_PRICE
                                ratingString -> SortBy.RATING
                                else -> SortBy.POPULARITY
                              },
                            startingPrice =
                              if (isPriceSwitchChecked) priceSliderPosition.start.toInt() else 1,
                            endingPrice =
                              if (isPriceSwitchChecked) priceSliderPosition.endInclusive.toInt()
                              else Int.MAX_VALUE,
                            condition =
                              when (conditions[checkedConditionIndex]) {
                                newString -> Condition.NEW
                                usedString -> Condition.USED
                                else -> {
                                  Condition.ANY
                                }
                              },
                            category =
                              if (categories[categoriesSelectionStates.indexOf(true)] != allString)
                                categories[categoriesSelectionStates.indexOf(true)]
                              else null,
                            collection =
                              if (
                                collections.isNotEmpty() &&
                                  collections[collectionsSelectionStates.indexOf(true)] != allString
                              )
                                collections[collectionsSelectionStates.indexOf(true)]
                              else null,
                            brands =
                              if (
                                isBrandSwitchChecked &&
                                  brands[brandsSelectionStates.indexOf(true)] != allString
                              )
                                brands.filterIndexed { index, _ -> brandsSelectionStates[index] }
                              else null,
                            rating =
                              if (isRatingSwitchChecked) ratingSliderPosition.toInt() else null,
                          )
                          ?.let { products ->
                            productsByCriteria.clear()
                            productsByCriteria.addAll(products)

                            priceSliderPosition =
                              (productsByCriteria.minOfOrNull {
                                  (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
                                } ?: 1)
                                .toFloat()..(productsByCriteria.maxOfOrNull {
                                    (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
                                  } ?: Int.MAX_VALUE)
                                  .toFloat()

                            if (brands.isNotEmpty()) brands.clear()
                            brands.addAll(productsByCriteria.map { it.brand!! }.distinct())
                            brandsSelectionStates.apply { repeat(brands.size) { add(false) } }
                          }

                        coroutineScope.launch {
                          sheetState.hide()
                          withContext(Dispatchers.Default) { showFiltersBottomSheet = false }
                        }
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
                Text(
                  text = stringResource(id = R.string.sort_by),
                  modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                  fontWeight = FontWeight.Medium,
                  style = MaterialTheme.typography.titleMedium,
                )
              }

              item {
                FlowRow(
                  modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                  horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                  sorts.forEachIndexed { sortIndex, sort ->
                    FilterChip(
                      selected = sortsSelectionStates[sortIndex],
                      onClick = {
                        sortsSelectionStates.forEachIndexed { sortsSelectionStatesIndex, _ ->
                          sortsSelectionStates[sortsSelectionStatesIndex] =
                            sortsSelectionStatesIndex == sortIndex
                        }
                      },
                      label = { Text(text = sort) },
                      leadingIcon = {
                        if (sortsSelectionStates[sortIndex])
                          Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                          )
                      },
                    )
                  }
                }
              }

              item {
                HorizontalDivider(
                  modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )
              }

              item {
                Column(modifier = Modifier.animateContentSize()) {
                  Row(
                    modifier =
                      Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                  ) {
                    Text(
                      text = stringResource(id = R.string.price),
                      fontWeight = FontWeight.Medium,
                      style = MaterialTheme.typography.titleMedium,
                    )

                    Row(
                      modifier = Modifier.padding(start = 8.dp),
                      verticalAlignment = Alignment.CenterVertically,
                    ) {
                      if (isPriceSwitchChecked) {
                        Text(
                          text =
                            "${priceSliderPosition.start.toInt().formatAsCurrency()} ${stringResource(id = R.string.to)} ${priceSliderPosition.endInclusive.toInt().formatAsCurrency()}",
                          color = SwitchDefaults.colors().checkedTrackColor,
                          style = MaterialTheme.typography.labelLarge,
                        )
                      }

                      Switch(
                        checked = isPriceSwitchChecked,
                        onCheckedChange = { isPriceSwitchChecked = !isPriceSwitchChecked },
                        modifier = Modifier.padding(start = 8.dp),
                      )
                    }
                  }

                  if (isPriceSwitchChecked) {
                    Text(
                      text = stringResource(id = R.string.price_supporting_text),
                      modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      style = MaterialTheme.typography.bodyMedium,
                    )

                    RangeSlider(
                      value = priceSliderPosition,
                      onValueChange = { priceSliderPosition = it },
                      modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                      valueRange =
                        (productsByCriteria.minOfOrNull {
                            (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
                          } ?: 1)
                          .toFloat()..(productsByCriteria.maxOfOrNull {
                              (it.price!! * ((100 - it.discountPercentage!!) / 100f)).toInt()
                            } ?: Int.MAX_VALUE)
                            .toFloat(),
                      colors =
                        SliderDefaults.colors(
                          thumbColor = MaterialTheme.colorScheme.secondary,
                          activeTrackColor = MaterialTheme.colorScheme.secondary,
                          inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                    )
                  }
                }
              }

              item {
                HorizontalDivider(
                  modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )
              }

              item {
                Text(
                  text = stringResource(id = R.string.condition),
                  modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                  fontWeight = FontWeight.Medium,
                  style = MaterialTheme.typography.titleMedium,
                )
              }

              item {
                MultiChoiceSegmentedButtonRow(
                  modifier =
                    Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp)
                ) {
                  repeat(conditions.size) { condition ->
                    SegmentedButton(
                      checked = checkedConditionIndex == condition,
                      onCheckedChange = { checkedConditionIndex = condition },
                      shape =
                        SegmentedButtonDefaults.itemShape(
                          index = condition,
                          count = conditions.size,
                        ),
                    ) {
                      Text(text = conditions[condition])
                    }
                  }
                }
              }

              item {
                HorizontalDivider(
                  modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )
              }

              item {
                Text(
                  text = stringResource(id = R.string.category),
                  modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                  fontWeight = FontWeight.Medium,
                  style = MaterialTheme.typography.titleMedium,
                )
              }

              item {
                FlowRow(
                  modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                  horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                  categories.forEachIndexed { categoryIndex, category ->
                    FilterChip(
                      selected = categoriesSelectionStates[categoryIndex],
                      onClick = {
                        categoriesSelectionStates[categoryIndex] =
                          !categoriesSelectionStates[categoryIndex]
                        categoriesSelectionStates.forEachIndexed { categoriesSelectionStatesIndex, _
                          ->
                          categoriesSelectionStates[categoriesSelectionStatesIndex] =
                            categoriesSelectionStatesIndex == categoryIndex
                        }

                        when (categories[categoryIndex]) {
                          resources.getString(R.string.all) -> {
                            collections.clear()
                            collectionsSelectionStates.clear()
                          }
                          resources.getString(R.string.apparel) -> {
                            collections.clear()
                            collectionsSelectionStates.clear()
                            collections.addAll(
                              listOf(
                                resources.getString(R.string.all),
                                resources.getString(R.string.academic_regalia),
                                resources.getString(R.string.backpacks_and_totes),
                                resources.getString(R.string.lab_coats_and_safety_goggles),
                              )
                            )
                          }
                          resources.getString(R.string.books) -> {
                            collections.clear()
                            collectionsSelectionStates.clear()
                            collections.addAll(
                              listOf(
                                resources.getString(R.string.all),
                                resources.getString(R.string.applied_sciences),
                                resources.getString(R.string.formal_sciences),
                                resources.getString(R.string.natural_sciences),
                                resources.getString(R.string.social_sciences),
                              )
                            )
                          }
                          resources.getString(R.string.electronics) -> {
                            collections.clear()
                            collectionsSelectionStates.clear()
                            collections.addAll(
                              listOf(
                                resources.getString(R.string.all),
                                resources.getString(R.string.adapters_and_cables),
                                resources.getString(R.string.calculators),
                                resources.getString(R.string.chargers_and_batteries),
                                resources.getString(R.string.docking_stations_and_usb_hubs),
                                resources.getString(R.string.ereaders),
                                resources.getString(R.string.external_storage_devices),
                                resources.getString(R.string.graphic_tablets),
                                resources.getString(R.string.headphones_and_earphones),
                                resources.getString(R.string.laptops),
                                resources.getString(R.string.mice),
                                resources.getString(R.string.power_banks),
                                resources.getString(R.string.printers),
                              )
                            )
                          }
                          resources.getString(R.string.materials) -> {
                            collections.clear()
                            collectionsSelectionStates.clear()
                            collections.addAll(
                              listOf(
                                resources.getString(R.string.all),
                                resources.getString(R.string.art),
                                resources.getString(R.string.engineering),
                                resources.getString(R.string.laboratory),
                                resources.getString(R.string.stationery),
                              )
                            )
                          }
                        }

                        collectionsSelectionStates.apply {
                          repeat(collections.size) { if (it != 0) add(false) else add(true) }
                        }
                      },
                      label = { Text(text = category) },
                      leadingIcon = {
                        if (categoriesSelectionStates[categoryIndex])
                          Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                          )
                      },
                    )
                  }
                }
              }

              item {
                HorizontalDivider(
                  modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )
              }

              item {
                Column(modifier = Modifier.animateContentSize()) {
                  Text(
                    text = stringResource(id = R.string.collection),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium,
                  )

                  if (collections.isEmpty()) {
                    Text(
                      text = stringResource(id = R.string.collection_supporting_text),
                      modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      style = MaterialTheme.typography.bodyMedium,
                    )
                  } else {
                    FlowRow(
                      modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                      horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                      collections.forEachIndexed { collectionIndex, collection ->
                        FilterChip(
                          selected = collectionsSelectionStates[collectionIndex],
                          onClick = {
                            collectionsSelectionStates.forEachIndexed {
                              collectionsSelectionStatesIndex,
                              _ ->
                              collectionsSelectionStates[collectionsSelectionStatesIndex] =
                                collectionsSelectionStatesIndex == collectionIndex
                            }
                          },
                          label = { Text(text = collection) },
                          leadingIcon = {
                            if (collectionsSelectionStates[collectionIndex])
                              Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize),
                              )
                          },
                        )
                      }
                    }
                  }

                  HorizontalDivider(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                  )
                }
              }

              item {
                Column(modifier = Modifier.animateContentSize()) {
                  Row(
                    modifier =
                      Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                  ) {
                    Text(
                      text = stringResource(id = R.string.brand),
                      fontWeight = FontWeight.Medium,
                      style = MaterialTheme.typography.titleMedium,
                    )

                    Switch(
                      checked = isBrandSwitchChecked,
                      onCheckedChange = { isBrandSwitchChecked = !isBrandSwitchChecked },
                      modifier = Modifier.padding(start = 8.dp),
                    )
                  }

                  if (isBrandSwitchChecked)
                    FlowRow(
                      modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                      horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                      brands.forEachIndexed { brandIndex, brand ->
                        FilterChip(
                          selected = brandsSelectionStates[brandIndex],
                          onClick = {
                            brandsSelectionStates[brandIndex] = !brandsSelectionStates[brandIndex]
                          },
                          label = { Text(text = brand) },
                          leadingIcon = {
                            if (brandsSelectionStates[brandIndex])
                              Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize),
                              )
                          },
                        )
                      }
                    }
                }
              }

              item {
                HorizontalDivider(
                  modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )
              }

              item {
                Column(modifier = Modifier.animateContentSize()) {
                  Row(
                    modifier =
                      Modifier.fillMaxWidth()
                        .padding(
                          start = 16.dp,
                          top = 16.dp,
                          end = 16.dp,
                          bottom = if (!isRatingSwitchChecked) 16.dp else 0.dp,
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                  ) {
                    Text(
                      text = stringResource(id = R.string.rating),
                      fontWeight = FontWeight.Medium,
                      style = MaterialTheme.typography.titleMedium,
                    )

                    Row(
                      modifier = Modifier.padding(start = 8.dp),
                      verticalAlignment = Alignment.CenterVertically,
                    ) {
                      val starsCount = ratingSliderPosition.toInt()

                      if (isRatingSwitchChecked) {
                        Text(
                          text =
                            "$starsCount ${if (starsCount > 1) stringResource(id = R.string.stars) else stringResource(id = R.string.star)}",
                          color = SwitchDefaults.colors().checkedTrackColor,
                          style = MaterialTheme.typography.labelLarge,
                        )
                      }

                      Switch(
                        checked = isRatingSwitchChecked,
                        onCheckedChange = { isRatingSwitchChecked = !isRatingSwitchChecked },
                        modifier = Modifier.padding(start = 8.dp),
                      )
                    }
                  }

                  if (isRatingSwitchChecked) {
                    Text(
                      text = stringResource(id = R.string.rating_supporting_text),
                      modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      style = MaterialTheme.typography.bodyMedium,
                    )

                    Slider(
                      value = ratingSliderPosition,
                      onValueChange = { ratingSliderPosition = it },
                      modifier = Modifier.padding(all = 16.dp),
                      colors =
                        SliderDefaults.colors(
                          thumbColor = MaterialTheme.colorScheme.secondary,
                          activeTrackColor = MaterialTheme.colorScheme.secondary,
                          inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                      steps = 3,
                      valueRange = 1f..5f,
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  ) { currentContentPadding ->
    val layoutDirection = LocalLayoutDirection.current

    LazyVerticalStaggeredGrid(
      columns = StaggeredGridCells.Fixed(2),
      modifier = Modifier.fillMaxSize().haze(state = hazeState),
      contentPadding =
        PaddingValues(
          start = currentContentPadding.calculateStartPadding(layoutDirection) + 16.dp,
          end = currentContentPadding.calculateEndPadding(layoutDirection) + 16.dp,
          bottom =
            contentPadding.calculateBottomPadding() +
              16.dp +
              WindowInsets.ime.asPaddingValues(LocalDensity.current).calculateBottomPadding().run {
                if (this > 0.dp) return@run this - currentContentPadding.calculateBottomPadding()
                else return@run 0.dp
              },
        ),
      verticalItemSpacing = 16.dp,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      item(span = StaggeredGridItemSpan.FullLine) {
        Spacer(
          modifier = Modifier.fillMaxWidth().height(currentContentPadding.calculateTopPadding())
        )
      }

      itemsIndexed(
        items = productsByCriteria,
        key = { _, productItem -> productItem.hashCode() },
      ) { _, product ->
        ListingCard(
          product = product,
          appViewModel = appViewModel,
          navHostController = navHostController,
        )
      }
    }
  }
}
