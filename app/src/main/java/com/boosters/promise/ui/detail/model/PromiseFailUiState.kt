package com.boosters.promise.ui.detail.model

import androidx.annotation.StringRes

data class PromiseFailUiState(
    @StringRes val messageResId: Int,
    val isActivityFinish: Boolean
)