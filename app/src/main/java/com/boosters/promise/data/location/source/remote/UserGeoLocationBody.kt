package com.boosters.promise.data.location.source.remote

import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.location.UserGeoLocation

data class UserGeoLocationBody(
    val userCode: String? = null,
    val geoLocation: GeoLocation? = null
)

fun UserGeoLocationBody.toUserGeoLocation() =
    UserGeoLocation(
        userCode = userCode ?: throw NullPointerException(),
        geoLocation = geoLocation
    )