package com.boosters.promise.ui.signup.model

import androidx.annotation.StringRes

data class NameInputUiState(
    val isNameValidationFail: Boolean = false,
    @StringRes val nameValidationErrorTextResId: Int? = null
)