package seifemadhamdy.supplera.main.shop.global.data.models

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class Collection(
  @ColorInt val containerColor: Int? = null,
  @ColorInt val textColor: Int? = null,
  val text: String,
  @DrawableRes val resourceId: Int? = null,
  val contentDescription: String? = null,
  val picassoMode: Boolean = true,
)
