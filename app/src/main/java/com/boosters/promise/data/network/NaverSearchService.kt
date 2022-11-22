package com.boosters.promise.data.network

import com.boosters.promise.data.place.source.remote.PlaceResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverSearchService {

    @GET("v1/search/local.json")
    suspend fun searchPlace(
        @Query("query") query: String,
        @Query("display") display: Int? = null
    ): PlaceResponseBody

}