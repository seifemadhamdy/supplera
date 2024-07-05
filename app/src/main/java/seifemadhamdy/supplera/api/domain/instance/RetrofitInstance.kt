package seifemadhamdy.supplera.api.domain.instance

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import seifemadhamdy.supplera.api.domain.base.SuppleraApi

object RetrofitInstance {
  private val suppleraApi: Retrofit by lazy {
    Retrofit.Builder()
      .baseUrl("https://drum-pretty-jay.ngrok-free.app/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  val suppleraApiBasket: SuppleraApi.BasketEndpoint by lazy {
    suppleraApi.create(SuppleraApi.BasketEndpoint::class.java)
  }

  val suppleraApiBasketItem: SuppleraApi.BasketItemEndpoint by lazy {
    suppleraApi.create(SuppleraApi.BasketItemEndpoint::class.java)
  }

  val suppleraApiCategory: SuppleraApi.CategoryEndpoint by lazy {
    suppleraApi.create(SuppleraApi.CategoryEndpoint::class.java)
  }

  val suppleraApiCollection: SuppleraApi.CollectionEndpoint by lazy {
    suppleraApi.create(SuppleraApi.CollectionEndpoint::class.java)
  }

  val suppleraApiCustomer: SuppleraApi.CustomerEndpoint by lazy {
    suppleraApi.create(SuppleraApi.CustomerEndpoint::class.java)
  }

  val suppleraApiHighlight: SuppleraApi.HighlightEndpoint by lazy {
    suppleraApi.create(SuppleraApi.HighlightEndpoint::class.java)
  }

  val suppleraApiPhoto: SuppleraApi.PhotoEndpoint by lazy {
    suppleraApi.create(SuppleraApi.PhotoEndpoint::class.java)
  }

  val suppleraApiProduct: SuppleraApi.ProductEndpoint by lazy {
    suppleraApi.create(SuppleraApi.ProductEndpoint::class.java)
  }

  val suppleraApiReview: SuppleraApi.ReviewEndpoint by lazy {
    suppleraApi.create(SuppleraApi.ReviewEndpoint::class.java)
  }

  val suppleraApiSale: SuppleraApi.SaleEndpoint by lazy {
    suppleraApi.create(SuppleraApi.SaleEndpoint::class.java)
  }
}
