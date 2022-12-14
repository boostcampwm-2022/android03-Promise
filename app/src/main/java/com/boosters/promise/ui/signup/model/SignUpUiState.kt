package com.boosters.promise.ui.signup.model

import androidx.annotation.StringRes

sealed interface SignUpUiState {

    object Loading : SignUpUiState
    object Success : SignUpUiState
    data class Fail(@StringRes val signUpErrorMessageResId: Int? = null) : SignUpUiState

}