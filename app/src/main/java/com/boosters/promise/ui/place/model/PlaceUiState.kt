package com.boosters.promise.ui.place.model

import com.boosters.promise.data.place.Place

data class PlaceUiState(
    val title: String = "",
    val link: String = "",
    val address: String = "",
    val roadAddress: String = "",
    val x: Int = 0,
    val y: Int = 0
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