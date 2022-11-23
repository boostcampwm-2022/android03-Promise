package com.boosters.promise.data.location

import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationRepository {

    private val _isReceivingLocationUpdates = MutableStateFlow(false)
    override val isReceivingLocationUpdates: StateFlow<Boolean>
        get() = _isReceivingLocationUpdates.asStateFlow()

    private val _lastGeoLocation = MutableStateFlow<GeoLocation?>(null)
    override val lastGeoLocation: StateFlow<GeoLocation?>
        get() = _lastGeoLocation.asStateFlow()

    val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                _lastGeoLocation.value = location.toGeoLocation()
            }
        }
    }

    override fun startLocationUpdates(looper: Looper): Result<Unit> = runCatching {
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
        _isReceivingLocationUpdates.value = true
    }

    override fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(callback)
        _isReceivingLocationUpdates.value = false
        _lastGeoLocation.value = null
    }

    private fun Location.toGeoLocation() =
        GeoLocation(
            latitude,
            longitude
        )

    companion object {
        private const val INTERVAL_MILLIS = 10_000L
        private const val MIN_UPDATE_DISTANCE_METERS = 5f
    }

}