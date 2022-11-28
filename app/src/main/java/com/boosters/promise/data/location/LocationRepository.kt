package com.boosters.promise.data.location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {

    val locationUpdateRequestCount: StateFlow<Int>

    val lastGeoLocation: StateFlow<GeoLocation?>

    fun startLocationUpdates(): Result<Unit>

    fun stopLocationUpdates()

    suspend fun uploadMyGeoLocation(geoLocation: GeoLocation): Result<Unit>

    suspend fun resetMyGeoLocation()

    fun getGeoLocation(userCode: String): Flow<UserGeoLocation>

    fun getGeoLocations(userCodes: List<String>): Flow<List<UserGeoLocation>>

}