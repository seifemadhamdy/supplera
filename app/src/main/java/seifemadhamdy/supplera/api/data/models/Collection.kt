package seifemadhamdy.supplera.api.data.models

import com.google.gson.annotations.SerializedName

data class Collection(
  @SerializedName("id") val id: Int? = null,
  @SerializedName("name") val name: String? = null,
  @SerializedName("categoryId") val categoryId: Int? = null,
)
