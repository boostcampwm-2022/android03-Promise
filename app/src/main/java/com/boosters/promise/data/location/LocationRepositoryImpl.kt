package com.boosters.promise.data.location

import android.location.Location
import android.os.HandlerThread
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationRepository {

    private val _locationUpdateRequestCount = MutableStateFlow(0)
    override val locationUpdateRequestCount: StateFlow<Int> = _locationUpdateRequestCount

    private val _lastGeoLocation = MutableStateFlow<GeoLocation?>(null)
    override val lastGeoLocation: StateFlow<GeoLocation?> = _lastGeoLocation.asStateFlow()

    val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                _lastGeoLocation.value = location.toGeoLocation()
            }
        }
    }

    override fun startLocationUpdates(): Result<Unit> = runCatching {
        if (locationUpdateRequestCount.value > 0) {
            _locationUpdateRequestCount.update { locationUpdateRequestCount.value + 1 }
            return Result.success(Unit)
        }

        val looper = HandlerThread(LOCATION_UPDATES_HANDLER_NAME).apply { start() }.looper
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, INTERVAL_MILLIS)
            .setMinUpdateDistanceMeters(MIN_UPDATE_DISTANCE_METERS)
            .build()

        try {
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                callback,
                looper
            )
        } catch (e: SecurityException) {
            throw e
        }

        _locationUpdateRequestCount.update { locationUpdateRequestCount.value + 1 }
    }

    override fun stopLocationUpdates() {
        _locationUpdateRequestCount.update { (locationUpdateRequestCount.value - 1).coerceAtLeast(0) }
        if (locationUpdateRequestCount.value < 1) {
            fusedLocationProviderClient.removeLocationUpdates(callback)
            _lastGeoLocation.value = null
        }
    }

    private fun Location.toGeoLocation() =
        GeoLocation(
            latitude,
            longitude
        )

    companion object {
        private const val INTERVAL_MILLIS = 10_000L
        private const val MIN_UPDATE_DISTANCE_METERS = 5f

        private const val LOCATION_UPDATES_HANDLER_NAME = "locationUpdatesHandler"
    }

}