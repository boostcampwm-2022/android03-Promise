package com.boosters.promise.ui.promise.model

import com.boosters.promise.data.place.Place
import com.boosters.promise.data.user.User
import com.boosters.promise.ui.place.model.PlaceUiState

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

fun PlaceUiState.toPlace(): Place {
    return Place(
        placeTitle = title,
        link = link,
        address = address,
        roadAddress = roadAddress,
        x = x,
        y = y
    )
}