package com.boosters.promise.data.place.source.remote

import com.boosters.promise.data.place.Place

interface PlaceRemoteDataSource {

    suspend fun searchPlace(query: String, display: Int): Result<List<Place?>>

}