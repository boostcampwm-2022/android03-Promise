package com.boosters.promise.ui.place.model

import com.boosters.promise.data.place.Place
import com.boosters.promise.data.promise.Location

data class PlaceUiState(
    val title: String = "",
    val link: String = "",
    val address: String = "",
    val roadAddress: String = "",
    val location: Location = Location()
)

fun PlaceUiState.toPlace(): Place {
    return Place(
        placeTitle = title,
        link = link,
        address = address,
        roadAddress = roadAddress,
        location = location
    )
}