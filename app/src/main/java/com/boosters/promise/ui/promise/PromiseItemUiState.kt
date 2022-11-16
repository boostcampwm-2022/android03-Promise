package com.boosters.promise.ui.promise

import com.boosters.promise.data.user.User

data class PromiseUiState(
    val id: String = "",
    val title: String = "",
    val destination: String = "",
    val destinationX: Int = 0,
    val destinationY: Int = 0,
    val date: String = "",
    val time: String = "",
    val members: List<User> = listOf()
)