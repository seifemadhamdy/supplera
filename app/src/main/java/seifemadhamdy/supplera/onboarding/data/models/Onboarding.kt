package seifemadhamdy.supplera.onboarding.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

data class Onboarding(
  @DrawableRes val resourceId: Int,
  @StringRes private val titleId: Int,
  @StringRes private val descriptionId: Int,
) {
  val title: String
    @Composable get() = stringResource(id = titleId)

  val description: String
    @Composable get() = stringResource(id = descriptionId)
}
