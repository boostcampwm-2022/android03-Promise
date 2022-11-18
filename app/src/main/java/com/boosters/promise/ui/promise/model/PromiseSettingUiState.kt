package com.boosters.promise.ui.promise.model

sealed class PromiseSettingUiState(open val promise: PromiseUiState = PromiseUiState()) {

    data class Empty(override val promise: PromiseUiState) : PromiseSettingUiState(promise)

    object Success: PromiseSettingUiState()

    data class Fail(override val promise: PromiseUiState): PromiseSettingUiState(promise)

}