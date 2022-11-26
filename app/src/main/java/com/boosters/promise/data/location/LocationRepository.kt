package com.boosters.promise.data.location

import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {

    val locationUpdateRequestCount: StateFlow<Int>

    val lastGeoLocation: StateFlow<GeoLocation?>

    fun startLocationUpdates(): Result<Unit>

    fun stopLocationUpdates()

}