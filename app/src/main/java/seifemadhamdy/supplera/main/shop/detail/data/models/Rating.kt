package seifemadhamdy.supplera.main.shop.detail.data.models

import seifemadhamdy.supplera.main.shop.detail.domain.helpers.Rater

data class Rating(val raters: Map<Rater, Int?>)
