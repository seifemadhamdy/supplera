package seifemadhamdy.supplera.authentication.global.domain.validators

fun isPasswordValid(password: String): Boolean =
  password.run {
    any { length >= 8 } && any { it.isDigit() }
    any { it.isUpperCase() } && any { it.isLowerCase() } && any { !it.isLetterOrDigit() }
  }
