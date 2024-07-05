package seifemadhamdy.supplera.main.me.sell.domain.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun encodeBitmapToBase64(bitmap: Bitmap): String {
  val byteArrayOutputStream = ByteArrayOutputStream()
  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
  return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}
