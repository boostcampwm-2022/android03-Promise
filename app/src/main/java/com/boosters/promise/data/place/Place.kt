package com.boosters.promise.data.place

import com.boosters.promise.data.location.GeoLocation

data class Place(
    val title: String = "",
    val link: String = "",
    val address: String = "",
    val roadAddress: String = "",
    val geoLocation: GeoLocation = GeoLocation()
)