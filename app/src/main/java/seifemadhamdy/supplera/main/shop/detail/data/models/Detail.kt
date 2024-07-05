package seifemadhamdy.supplera.main.shop.detail.data.models

import seifemadhamdy.supplera.api.data.models.Highlight
import seifemadhamdy.supplera.api.data.models.Photo
import seifemadhamdy.supplera.api.data.models.Review

data class Detail(
  val productName: String,
  val productDescription: String,
  val photos: List<Photo>,
  val productPrice: Int,
  val discountPercentage: Int = 0,
  val highlights: List<Highlight>,
  val reviews: List<Review>,
)
