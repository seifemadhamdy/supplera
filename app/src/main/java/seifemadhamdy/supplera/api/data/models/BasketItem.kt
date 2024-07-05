package seifemadhamdy.supplera.api.data.models

import com.google.gson.annotations.SerializedName

data class BasketItem(
  @SerializedName("id") val id: Int? = null,
  @SerializedName("basketId") val basketId: Int? = null,
  @SerializedName("productId") val productId: Int? = null,
)
