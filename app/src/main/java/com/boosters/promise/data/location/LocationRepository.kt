package com.boosters.promise.data.location

import android.os.Looper
import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {

    val isReceivingLocationUpdates: StateFlow<Boolean>

    val lastGeoLocation: StateFlow<GeoLocation?>

    fun startLocationUpdates(looper: Looper): Result<Unit>

    fun stopLocationUpdates()

}