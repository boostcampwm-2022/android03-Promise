package com.boosters.promise.ui.signup.model

import androidx.annotation.StringRes

data class SignUpUiState(
    val isRegistering: Boolean,
    @StringRes val errorText: Int?
)
