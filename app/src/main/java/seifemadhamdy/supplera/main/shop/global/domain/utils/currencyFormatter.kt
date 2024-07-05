package seifemadhamdy.supplera.main.shop.global.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import seifemadhamdy.supplera.R
import java.text.NumberFormat
import java.util.Currency

@Composable
fun Int.formatAsCurrency(): String =
  NumberFormat.getCurrencyInstance().run {
    maximumFractionDigits = 0
    currency = Currency.getInstance(stringResource(id = R.string.egp))
    format(this@formatAsCurrency)
  }
