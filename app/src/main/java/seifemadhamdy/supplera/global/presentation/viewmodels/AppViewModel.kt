package seifemadhamdy.supplera.global.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
import seifemadhamdy.supplera.api.domain.instance.RetrofitInstance
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val Context.userStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class AppViewModel : ViewModel() {
  suspend fun createBasket(basket: Basket): Basket? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiBasket
        .createBasket(basket = basket)
        .enqueue(
          object : Callback<Basket> {
            override fun onResponse(call: Call<Basket>, response: Response<Basket>) {
              if (response.isSuccessful) {
                if (response.isSuccessful) {
                  continuation.resume(response.body())
                } else {
                  continuation.resume(null)
                }
              }
            }

            override fun onFailure(call: Call<Basket>, throwable: Throwable) {
              throwable.message?.let { Log.i("createBasket", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getLastBasketByCustomerId(customerId: Int): Basket? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiBasket
        .getLastBasketByCustomerId(customerId = customerId)
        .enqueue(
          object : Callback<Basket> {
            override fun onResponse(call: Call<Basket>, response: Response<Basket>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<Basket>, throwable: Throwable) {
              throwable.message?.let { Log.i("getLastBasketByCustId", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  fun createBasketItem(basketItem: BasketItem) {
    RetrofitInstance.suppleraApiBasketItem
      .createBasketItem(basketItem = basketItem)
      .enqueue(
        object : Callback<Void> {
          override fun onResponse(call: Call<Void>, response: Response<Void>) {}

          override fun onFailure(call: Call<Void>, t: Throwable) {}
        }
      )
  }

  suspend fun getAllBasketItemsByBasketId(basketId: Int): List<BasketItem>? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiBasketItem
        .getAllBasketItemsByBasketId(basketId = basketId)
        .enqueue(
          object : Callback<List<BasketItem>> {
            override fun onResponse(
              call: Call<List<BasketItem>>,
              response: Response<List<BasketItem>>,
            ) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<List<BasketItem>>, throwable: Throwable) {
              throwable.message?.let { Log.i("getBasketItemsByBsktId", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  fun deleteBasketItemById(id: Int) {
    RetrofitInstance.suppleraApiBasketItem
      .deleteBasketItemById(id = id)
      .enqueue(
        object : Callback<Void> {
          override fun onResponse(call: Call<Void>, response: Response<Void>) {}

          override fun onFailure(call: Call<Void>, t: Throwable) {}
        }
      )
  }

  suspend fun getAllCategories(): List<Category>? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiCategory
        .getAllCategories()
        .enqueue(
          object : Callback<List<Category>> {
            override fun onResponse(
              call: Call<List<Category>>,
              response: Response<List<Category>>,
            ) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<List<Category>>, throwable: Throwable) {
              throwable.message?.let { Log.i("getAllCategories", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getAllCollections(): List<Collection>? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiCollection
        .getAllCollections()
        .enqueue(
          object : Callback<List<Collection>> {
            override fun onResponse(
              call: Call<List<Collection>>,
              response: Response<List<Collection>>,
            ) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<List<Collection>>, throwable: Throwable) {
              throwable.message?.let { Log.i("getAllCollections", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getAllCollectionsByCategoryId(categoryId: Int): List<Collection>? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiCollection
        .getAllCollectionsByCategoryId(categoryId = categoryId)
        .enqueue(
          object : Callback<List<Collection>> {
            override fun onResponse(
              call: Call<List<Collection>>,
              response: Response<List<Collection>>,
            ) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<List<Collection>>, throwable: Throwable) {
              throwable.message?.let { Log.i("getCollectionsByCatId", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  fun createCustomer(customer: Customer) {
    RetrofitInstance.suppleraApiCustomer
      .createCustomer(customer = customer)
      .enqueue(
        object : Callback<Void> {
          override fun onResponse(call: Call<Void>, response: Response<Void>) {}

          override fun onFailure(call: Call<Void>, t: Throwable) {}
        }
      )
  }

  suspend fun getCustomerByEmail(email: String): Customer? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiCustomer
        .getCustomerByEmail(email = email)
        .enqueue(
          object : Callback<Customer> {
            override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<Customer>, throwable: Throwable) {
              throwable.message?.let { Log.i("getCustomerByEmail", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  fun updateCustomer(customer: Customer) {
    RetrofitInstance.suppleraApiCustomer
      .updateCustomer(customer = customer)
      .enqueue(
        object : Callback<Void> {
          override fun onResponse(call: Call<Void>, response: Response<Void>) {}

          override fun onFailure(call: Call<Void>, t: Throwable) {}
        }
      )
  }

  suspend fun getAllHighlightsByProductId(productId: Int): List<Highlight>? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiHighlight
        .getAllHighlightsByProductId(productId = productId)
        .enqueue(
          object : Callback<List<Highlight>> {
            override fun onResponse(
              call: Call<List<Highlight>>,
              response: Response<List<Highlight>>,
            ) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<List<Highlight>>, throwable: Throwable) {
              throwable.message?.let { Log.i("getAllHighByProdId", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getAllPhotosByProductId(productId: Int): List<Photo>? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiPhoto
        .getAllPhotosByProductId(productId = productId)
        .enqueue(
          object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<List<Photo>>, throwable: Throwable) {
              throwable.message?.let { Log.i("getAllPhotosByProductId", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getAllProducts(): List<Product>? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiProduct
        .getAllProducts()
        .enqueue(
          object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<List<Product>>, throwable: Throwable) {
              throwable.message?.let { Log.i("getAllCategories", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getProductById(id: Int): Product? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiProduct
        .getProductById(id = id)
        .enqueue(
          object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<Product>, throwable: Throwable) {
              throwable.message?.let { Log.i("getProductById", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getAllProductsByCollectionId(collectionId: Int): List<Product>? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiProduct
        .getAllProductsByCollectionId(collectionId = collectionId)
        .enqueue(
          object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<List<Product>>, throwable: Throwable) {
              throwable.message?.let { Log.i("getProductsByColId", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getAllReviewsByProductId(productId: Int): List<Review>? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiReview
        .getAllReviewsByProductId(productId = productId)
        .enqueue(
          object : Callback<List<Review>> {
            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<List<Review>>, throwable: Throwable) {
              throwable.message?.let { Log.i("getProductsByColId", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getRatersCountWithFiveStarsForProduct(productId: Int): Int? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiReview
        .getRatersCountWithFiveStarsForProduct(productId = productId)
        .enqueue(
          object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<Int>, throwable: Throwable) {
              throwable.message?.let { Log.i("getRCountFiveForProd", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getRatersCountWithFourStarsForProduct(productId: Int): Int? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiReview
        .getRatersCountWithFourStarsForProduct(productId = productId)
        .enqueue(
          object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<Int>, throwable: Throwable) {
              throwable.message?.let { Log.i("getRCountFourForProd", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getRatersCountWithThreeStarsForProduct(productId: Int): Int? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiReview
        .getRatersCountWithThreeStarsForProduct(productId = productId)
        .enqueue(
          object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<Int>, throwable: Throwable) {
              throwable.message?.let { Log.i("getRCountThreeForProd", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getRatersCountWithTwoStarsForProduct(productId: Int): Int? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiReview
        .getRatersCountWithTwoStarsForProduct(productId = productId)
        .enqueue(
          object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<Int>, throwable: Throwable) {
              throwable.message?.let { Log.i("getRCountTwoForProd", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  suspend fun getRatersCountWithOneStarForProduct(productId: Int): Int? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiReview
        .getRatersCountWithOneStarForProduct(productId = productId)
        .enqueue(
          object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<Int>, throwable: Throwable) {
              throwable.message?.let { Log.i("getRCountOneForProd", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  fun createSale(sale: Sale) {
    RetrofitInstance.suppleraApiSale
      .createSale(sale = sale)
      .enqueue(
        object : Callback<Void> {
          override fun onResponse(call: Call<Void>, response: Response<Void>) {}

          override fun onFailure(call: Call<Void>, t: Throwable) {}
        }
      )
  }

  suspend fun getAllSalesByCustomerId(customerId: Int): List<Sale>? {
    return suspendCoroutine { continuation ->
      RetrofitInstance.suppleraApiSale
        .getAllSalesByCustomerId(customerId = customerId)
        .enqueue(
          object : Callback<List<Sale>> {
            override fun onResponse(call: Call<List<Sale>>, response: Response<List<Sale>>) {
              if (response.isSuccessful) {
                continuation.resume(response.body())
              } else {
                continuation.resume(null)
              }
            }

            override fun onFailure(call: Call<List<Sale>>, throwable: Throwable) {
              throwable.message?.let { Log.i("getAllSalesByCustomerId", it) }
              continuation.resume(null)
            }
          }
        )
    }
  }

  @Suppress("PrivatePropertyName") private val STORED_USER_EMAIL = stringPreferencesKey("email")

  suspend fun getStoredUserEmail(context: Context) =
    context.userStore.data.map { preferences -> preferences[STORED_USER_EMAIL] }.firstOrNull()

  suspend fun storeUserEmail(context: Context, email: String) {
    context.userStore.edit { user -> user[STORED_USER_EMAIL] = email }
  }

  suspend fun clearUserEmail(context: Context) {
    context.userStore.edit { user -> user.remove(STORED_USER_EMAIL) }
  }
}
