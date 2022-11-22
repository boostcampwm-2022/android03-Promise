package com.boosters.promise.ui.promise.model

import androidx.annotation.StringRes

sealed class PromiseSettingUiState {

    object Edit : PromiseSettingUiState()

    object Success : PromiseSettingUiState()

    data class Fail(@StringRes val message: Int) : PromiseSettingUiState()

}