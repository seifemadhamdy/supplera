package seifemadhamdy.supplera.global.domain.navigation.destinations

sealed class AppDestination(val route: String) {
  data object Onboarding : AppDestination(route = "Onboarding")

  data object Join : AppDestination(route = "Join")

  data object SignIn : AppDestination(route = "SignIn")

  data object Main : AppDestination(route = "Main")

  data object Shop : AppDestination(route = "Shop")

  data object ProductListing :
    AppDestination(route = "ProductListing/{searchString}/{categoryString}/{collectionString}")

  data object ProductDetail : AppDestination(route = "ProductDetail/{productId}")

  data object Basket : AppDestination(route = "Basket")

  data object OrderPayment : AppDestination(route = "OrderPayment")

  data object OrderCheckout : AppDestination(route = "OrderCheckout/{isOrderToBePaidWithCash}")

  data object Me : AppDestination(route = "Me")

  data object Sell : AppDestination(route = "Sell")

  data object PreviousSubmissions : AppDestination(route = "PreviousSubmissions")
}
