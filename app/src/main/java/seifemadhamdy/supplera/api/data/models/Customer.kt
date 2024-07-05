package seifemadhamdy.supplera.api.data.models

data class Customer(
  val id: Int,
  val name: String,
  val email: String,
  val password: String,
  val phone: String,
  val address: String,
)
