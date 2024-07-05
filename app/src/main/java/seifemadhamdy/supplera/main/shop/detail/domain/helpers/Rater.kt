package seifemadhamdy.supplera.main.shop.detail.domain.helpers

enum class Rater(val ratingValue: Int) {
  FIVE_STAR(ratingValue = 5),
  FOUR_STAR(ratingValue = 4),
  THREE_STAR(ratingValue = 3),
  TWO_STAR(ratingValue = 2),
  ONE_STAR(ratingValue = 1);

  companion object {
    private val map = entries.toTypedArray().associateBy(Rater::ratingValue)

    fun fromRatingValue(ratingValue: Int) = map[ratingValue]
  }
}
