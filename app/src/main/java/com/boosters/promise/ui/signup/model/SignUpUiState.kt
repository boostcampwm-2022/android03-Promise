package com.boosters.promise.ui.signup.model

import androidx.annotation.StringRes

data class SignUpUiState(
    val isRegistering: Boolean = false,
    val isNameValidationFail: Boolean = false,
    @StringRes val errorText: Int? = null
)
