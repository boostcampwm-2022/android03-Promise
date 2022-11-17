package com.boosters.promise.data.place

interface PlaceRepository {

    suspend fun searchPlace(query: String, display: Int = 5): Result<List<Place>>

}