package seifemadhamdy.supplera.api.domain.base

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import seifemadhamdy.supplera.api.data.models.Basket
import seifemadhamdy.supplera.api.data.models.BasketItem
import seifemadhamdy.supplera.api.data.models.Category
import seifemadhamdy.supplera.api.data.models.Collection
import seifemadhamdy.supplera.api.data.models.Customer
import seifemadhamdy.supplera.api.data.models.Highlight
import seifemadhamdy.supplera.api.data.models.Photo
import seifemadhamdy.supplera.api.data.models.Product
import seifemadhamdy.supplera.api.data.models.Review
import seifemadhamdy.supplera.api.data.models.Sale

interface SuppleraApi {
  interface BasketEndpoint {
    @POST("/basket") fun createBasket(@Body basket: Basket): Call<Basket>

    @GET("/basket/customer/{customerId}/last")
    fun getLastBasketByCustomerId(@Path("customerId") customerId: Int): Call<Basket>
  }

  interface BasketItemEndpoint {
    @POST("/basket-item") fun createBasketItem(@Body basketItem: BasketItem): Call<Void>

    @GET("/basket-item/basket/{basketId}")
    fun getAllBasketItemsByBasketId(@Path("basketId") basketId: Int): Call<List<BasketItem>>

    @DELETE("/basket-item/{id}") fun deleteBasketItemById(@Path("id") id: Int): Call<Void>
  }

  interface CategoryEndpoint {
    @GET("/category") fun getAllCategories(): Call<List<Category>>
  }

  interface CollectionEndpoint {
    @GET("/collection") fun getAllCollections(): Call<List<Collection>>

    @GET("/collection/category/{categoryId}")
    fun getAllCollectionsByCategoryId(@Path("categoryId") categoryId: Int): Call<List<Collection>>
  }

  interface CustomerEndpoint {
    @POST("/customer") fun createCustomer(@Body customer: Customer): Call<Void>

    @GET("/customer/{email}") fun getCustomerByEmail(@Path("email") email: String): Call<Customer>

    @PUT("/customer") fun updateCustomer(@Body customer: Customer): Call<Void>
  }

  interface HighlightEndpoint {
    @GET("/highlight/product/{productId}")
    fun getAllHighlightsByProductId(@Path("productId") productId: Int): Call<List<Highlight>>
  }

  interface PhotoEndpoint {
    @GET("/photo/product/{productId}")
    fun getAllPhotosByProductId(@Path("productId") productId: Int): Call<List<Photo>>
  }

  interface ProductEndpoint {
    @GET("/product") fun getAllProducts(): Call<List<Product>>

    @GET("/product/{id}") fun getProductById(@Path("id") id: Int): Call<Product>

    @GET("/product/collection/{collectionId}")
    fun getAllProductsByCollectionId(@Path("collectionId") collectionId: Int): Call<List<Product>>
  }

  interface ReviewEndpoint {
    @GET("/review/product/{productId}")
    fun getAllReviewsByProductId(@Path("productId") productId: Int): Call<List<Review>>

    @GET("/review/product/{productId}/five-star-raters-count/")
    fun getRatersCountWithFiveStarsForProduct(@Path("productId") productId: Int): Call<Int>

    @GET("/review/product/{productId}/four-star-raters-count/")
    fun getRatersCountWithFourStarsForProduct(@Path("productId") productId: Int): Call<Int>

    @GET("/review/product/{productId}/three-star-raters-count/")
    fun getRatersCountWithThreeStarsForProduct(@Path("productId") productId: Int): Call<Int>

    @GET("/review/product/{productId}/two-star-raters-count/")
    fun getRatersCountWithTwoStarsForProduct(@Path("productId") productId: Int): Call<Int>

    @GET("/review/product/{productId}/one-star-raters-count/")
    fun getRatersCountWithOneStarForProduct(@Path("productId") productId: Int): Call<Int>
  }

  interface SaleEndpoint {
    @POST("/sale") fun createSale(@Body sale: Sale): Call<Void>

    @GET("/sale/customer/{customerId}")
    fun getAllSalesByCustomerId(@Path("customerId") customerId: Int): Call<List<Sale>>
  }
}
