package seifemadhamdy.supplera.main.shop.listing.domain.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import seifemadhamdy.supplera.api.data.models.Product
import seifemadhamdy.supplera.global.presentation.viewmodels.AppViewModel
import seifemadhamdy.supplera.main.shop.detail.data.models.Rating
import seifemadhamdy.supplera.main.shop.detail.domain.helpers.Rater
import seifemadhamdy.supplera.main.shop.detail.domain.utils.RatingUtils
import seifemadhamdy.supplera.main.shop.listing.domain.helpers.Condition
import seifemadhamdy.supplera.main.shop.listing.domain.helpers.SortBy
import java.math.BigDecimal

suspend fun filterAllProductsBy(
  appViewModel: AppViewModel,
  sortBy: SortBy = SortBy.POPULARITY,
  startingPrice: Int = 1,
  endingPrice: Int = Int.MAX_VALUE,
  condition: Condition = Condition.ANY,
  category: String? = null,
  collection: String? = null,
  brands: List<String>? = null,
  rating: Int? = null,
): List<Product>? {
  return withContext(Dispatchers.IO) {
    val selectedCategory =
      if (category != null) appViewModel.getAllCategories()?.find { it.name == category } else null

    val selectedCollection =
      if (collection != null) appViewModel.getAllCollections()?.find { it.name == collection }
      else null

    val selectedCategoryCollectionsIds =
      if (selectedCategory != null && selectedCollection == null)
        selectedCategory.id?.let { selectedCategoryId ->
          appViewModel.getAllCollectionsByCategoryId(categoryId = selectedCategoryId)?.map { it.id }
        }
      else null

    appViewModel
      .getAllProducts()
      ?.filter { product ->
        (product.price!! * ((100 - product.discountPercentage!!) / 100f)).toInt() in
          startingPrice..endingPrice &&
          when (condition) {
            Condition.ANY -> true
            Condition.NEW -> product.isUsed == false
            Condition.USED -> product.isUsed == true
          } &&
          if (selectedCollection != null) product.collectionId == selectedCollection.id
          else
            if (selectedCategoryCollectionsIds != null) {
              product.collectionId in selectedCategoryCollectionsIds
            } else {
              true
            } &&
              if (brands != null) {
                product.brand in brands
              } else {
                true
              } &&
              if (rating != null) {
                calculateRatingScore(appViewModel = appViewModel, productId = product.id!!) >=
                  rating.toBigDecimal()
              } else {
                true
              }
      }
      ?.run {
        when (sortBy) {
          SortBy.ARRIVAL_DATE -> sortedByDescending { it.arrivalDate }
          SortBy.HIGHER_PRICE -> sortedByDescending { it.price }
          SortBy.LOWER_PRICE -> sortedBy { it.price }
          SortBy.POPULARITY -> sortedByDescending { it.popularityIndex }
          SortBy.RATING -> {
            val productsWithRatingScoresMap = mutableMapOf<Product, BigDecimal>()

            forEach { product ->
              productsWithRatingScoresMap[product] =
                calculateRatingScore(appViewModel = appViewModel, productId = product.id!!)
            }

            productsWithRatingScoresMap.toList().sortedByDescending { it.second }.map { it.first }
          }
        }
      }
  }
}

private suspend fun calculateRatingScore(appViewModel: AppViewModel, productId: Int) =
  RatingUtils(
      rating =
        Rating(
          raters =
            mapOf(
              Rater.FIVE_STAR to appViewModel.getRatersCountWithFiveStarsForProduct(productId),
              Rater.FOUR_STAR to appViewModel.getRatersCountWithFourStarsForProduct(productId),
              Rater.THREE_STAR to appViewModel.getRatersCountWithThreeStarsForProduct(productId),
              Rater.TWO_STAR to appViewModel.getRatersCountWithTwoStarsForProduct(productId),
              Rater.ONE_STAR to appViewModel.getRatersCountWithOneStarForProduct(productId),
            )
        )
    )
    .ratingScore
