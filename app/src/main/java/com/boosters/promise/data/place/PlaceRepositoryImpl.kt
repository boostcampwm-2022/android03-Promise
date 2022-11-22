package com.boosters.promise.data.place

import com.boosters.promise.data.place.source.remote.PlaceRemoteDataSource
import com.boosters.promise.data.place.source.remote.toPlace
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource
) : PlaceRepository {

    override suspend fun searchPlace(query: String, display: Int): Result<List<Place>> =
        placeRemoteDataSource.searchPlace(query, display).mapCatching { responseBody ->
            responseBody.items.mapNotNull { item ->
                item?.toPlace()
            }
        }

}