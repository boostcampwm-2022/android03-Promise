package com.boosters.promise.data.location

import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {

    val isReceivingLocationUpdates: StateFlow<Boolean>

    val isUploadMyLocation: StateFlow<Boolean>

    val lastGeoLocation: StateFlow<GeoLocation?>

    fun startLocationUpdates(): Result<Unit>

    fun stopLocationUpdates()

    fun setIsUploadMyLocation(isUploadMyLocation: Boolean)

}