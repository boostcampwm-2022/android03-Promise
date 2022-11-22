package com.boosters.promise.data.place.source.remote

interface PlaceRemoteDataSource {

    suspend fun searchPlace(query: String, display: Int): Result<PlaceResponseBody>

}