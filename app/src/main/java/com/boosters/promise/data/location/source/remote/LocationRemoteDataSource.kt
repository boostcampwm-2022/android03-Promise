package com.boosters.promise.data.location.source.remote

import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.location.di.LocationModule.LocationCollectionReference
import com.boosters.promise.data.network.NetworkConnectionUtil
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRemoteDataSource @Inject constructor(
    @LocationCollectionReference private val locationCollectionReference: CollectionReference,
    private val networkConnectionUtil: NetworkConnectionUtil
) {

    fun uploadGeoLocation(userCode: String, geoLocation: GeoLocation?): Result<Unit> = runCatching {
        networkConnectionUtil.checkNetworkOnline()

        locationCollectionReference
            .whereEqualTo(USER_CODE_KEY, userCode)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    locationCollectionReference.document().set(
                        UserGeoLocationBody(
                            userCode = userCode,
                            geoLocation = geoLocation
                        )
                    )
                } else {
                    locationCollectionReference.document(documents.first().id)
                        .update(GEO_LOCATION_FIELD, geoLocation)
                }
            }
    }

    fun resetMyGeoLocation(userCode: String) {
        uploadGeoLocation(userCode, null)
    }

    fun getGeoLocation(userCode: String): Flow<UserGeoLocationBody> =
        locationCollectionReference
            .document(userCode)
            .snapshots()
            .mapNotNull {
                it.toObject(UserGeoLocationBody::class.java)
            }

    fun getGeoLocations(userCodes: List<String>): Flow<List<UserGeoLocationBody>> =
        locationCollectionReference
            .whereEqualTo(USER_CODE_KEY, userCodes)
            .snapshots()
            .map {
                it.toObjects(UserGeoLocationBody::class.java)
            }

    private companion object {
        const val USER_CODE_KEY = "userCode"
        const val GEO_LOCATION_FIELD = "geoLocation"
    }
}