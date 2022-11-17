package com.boosters.promise.data.place

import com.boosters.promise.ui.place.model.PlaceUiState

data class Place(
    val placeTitle: String,
    val link: String,
    val address: String,
    val roadAddress: String,
    val x: Int,
    val y: Int
)

fun Place.toPlaceUiState(): PlaceUiState {
    return PlaceUiState(
        title = placeTitle,
        link = link,
        address = address,
        roadAddress = roadAddress,
        x = x,
        y = y
    )
}