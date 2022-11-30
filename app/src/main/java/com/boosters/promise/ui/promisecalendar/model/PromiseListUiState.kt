package com.boosters.promise.ui.promisecalendar.model

import com.boosters.promise.data.promise.Promise

sealed class PromiseListUiState {

    data class Success(val data: List<Promise>) : PromiseListUiState()

    object Loading : PromiseListUiState()

    object Empty : PromiseListUiState()

    data class Failure(val throwable: Throwable) : PromiseListUiState()

}