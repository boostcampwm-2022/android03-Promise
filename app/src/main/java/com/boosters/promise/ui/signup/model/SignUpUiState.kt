package com.boosters.promise.ui.signup.model

import androidx.annotation.StringRes

data class SignUpUiState(
    val isRegistering: Boolean = false,
    val isCompleteSignUp: Boolean = false,
    val isErrorSignUp: Boolean = false,
    @StringRes val signUpErrorMessageResId: Int? = null
)
