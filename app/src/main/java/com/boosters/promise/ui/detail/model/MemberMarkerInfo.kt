package com.boosters.promise.ui.detail.model

import com.boosters.promise.data.location.GeoLocation

data class MemberMarkerInfo(
    val id: String,
    val name: String,
    val geoLocation: GeoLocation
)