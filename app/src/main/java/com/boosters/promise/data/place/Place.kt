package com.boosters.promise.data.place

import com.boosters.promise.data.promise.Location
import com.boosters.promise.ui.place.model.PlaceUiState

data class Place(
    val placeTitle: String,
    val link: String,
    val address: String,
    val roadAddress: String,
    val location: Location
)

fun Place.toPlaceUiState(): PlaceUiState {
    return PlaceUiState(
        title = placeTitle,
        link = link,
        address = address,
        roadAddress = roadAddress,
        location = location
    )
}