package com.boosters.promise.data.location

import android.location.Location
import android.os.HandlerThread
import com.boosters.promise.data.location.source.remote.LocationRemoteDataSource
import com.boosters.promise.data.location.source.remote.toUserGeoLocation
import com.boosters.promise.data.user.source.local.MyInfoLocalDataSource
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val myInfoLocalDataSource: MyInfoLocalDataSource,
    private val locationRemoteDataSource: LocationRemoteDataSource
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

    override suspend fun uploadMyGeoLocation(geoLocation: GeoLocation): Result<Unit> = runCatching {
        val user = myInfoLocalDataSource.getMyInfo().first().getOrThrow()
        locationRemoteDataSource.uploadGeoLocation(user.userCode, geoLocation)
    }

    override suspend fun resetMyGeoLocation() {
        val user = myInfoLocalDataSource.getMyInfo().first().getOrNull()
        if (user != null) {
            locationRemoteDataSource.resetMyGeoLocation(user.userCode)
        }
    }

    override fun getGeoLocation(userCode: String): Flow<UserGeoLocation> {
        return locationRemoteDataSource.getGeoLocation(userCode).mapNotNull {
            try {
                it.toUserGeoLocation()
            } catch (e: NullPointerException) {
                null
            }
        }
    }

    override fun getGeoLocations(userCodes: List<String>): Flow<List<UserGeoLocation>> {
        return locationRemoteDataSource.getGeoLocations(userCodes).map { userGeoLocationBodies ->
            userGeoLocationBodies.mapNotNull {
                try {
                    it.toUserGeoLocation()
                } catch (e: NullPointerException) {
                    null
                }
            }
        }
    }

    companion object {
        private const val INTERVAL_MILLIS = 10_000L
        private const val MIN_UPDATE_DISTANCE_METERS = 5f

        private const val LOCATION_UPDATES_HANDLER_NAME = "locationUpdatesHandler"
    }

}