package seifemadhamdy.supplera.authentication.global.domain.validators

import android.util.Patterns

fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
