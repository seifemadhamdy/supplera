package seifemadhamdy.supplera.api.data.models

data class Product(
  val id: Int? = null,
  val name: String? = null,
  val brand: String? = null,
  val isUsed: Boolean? = null,
  val popularityIndex: Int? = null,
  val arrivalDate: String? = null,
  val price: Int? = null,
  val discountPercentage: Int? = null,
  val description: String? = null,
  val collectionId: Int? = null,
)
