package com.boosters.promise.data.place

import com.boosters.promise.data.model.Location

data class Place(
    val title: String = "",
    val link: String = "",
    val address: String = "",
    val roadAddress: String = "",
    val location: Location = Location()
)