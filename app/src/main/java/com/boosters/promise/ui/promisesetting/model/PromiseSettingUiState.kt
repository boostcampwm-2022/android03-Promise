package com.boosters.promise.ui.promisesetting.model

import androidx.annotation.StringRes

sealed class PromiseSettingUiState {

    object Loading : PromiseSettingUiState()

    object Edit : PromiseSettingUiState()

    object Success : PromiseSettingUiState()

    data class Fail(@StringRes val message: Int) : PromiseSettingUiState()

}