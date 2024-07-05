package seifemadhamdy.supplera.api.data.models

import com.google.gson.annotations.SerializedName

data class Basket(
  @SerializedName("id") val id: Int? = null,
  @SerializedName("customerId") val customerId: Int? = null,
)
