package com.boosters.promise.data.location

import android.os.Parcelable
import com.naver.maps.geometry.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeoLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Parcelable

fun GeoLocation.toLatLng() =
    LatLng(latitude, longitude)