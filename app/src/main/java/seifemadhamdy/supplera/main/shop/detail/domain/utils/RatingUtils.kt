package seifemadhamdy.supplera.main.shop.detail.domain.utils

import seifemadhamdy.supplera.main.shop.detail.data.models.Rating
import seifemadhamdy.supplera.main.shop.detail.domain.helpers.Rater
import java.math.BigDecimal
import java.math.RoundingMode

class RatingUtils(private val rating: Rating) {
  var totalRatingsCount = 0
  private var totalStarsCount = 0
  var ratingScore: BigDecimal

  init {
    rating.raters.forEach {
      totalRatingsCount += it.value ?: 0
      totalStarsCount += it.key.ratingValue * (it.value ?: 0)
    }

    ratingScore =
      BigDecimal(totalStarsCount.toDouble() / totalRatingsCount).setScale(1, RoundingMode.HALF_EVEN)
  }

  fun ratersPercentage(rater: Rater): Float? =
    rating.raters[rater]?.toFloat()?.div(totalRatingsCount)
}
