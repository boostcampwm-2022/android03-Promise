package com.boosters.promise.ui.promisecalendar.model

import com.boosters.promise.data.user.User

sealed class UserUiState {
    data class Success(val data: User) : UserUiState()
    object Loading : UserUiState()
    object Empty : UserUiState()
    data class Failure(val throwable: Throwable) : UserUiState()
}