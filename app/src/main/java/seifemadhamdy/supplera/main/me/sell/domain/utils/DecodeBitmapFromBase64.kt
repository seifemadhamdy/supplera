package seifemadhamdy.supplera.main.me.sell.domain.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun decodeBitmapFromBase64(string: String): Bitmap? {
  return try {
    val byteArray = Base64.decode(string, Base64.DEFAULT)
    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
  } catch (illegalArgumentException: IllegalArgumentException) {
    null
  }
}
