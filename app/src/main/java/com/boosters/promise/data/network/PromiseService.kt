package com.boosters.promise.data.network

import com.boosters.promise.data.promise.source.remote.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PromiseService {

    @GET("v1/search/local.json")
    suspend fun searchLocalQuery(
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null
    ): PlaceResponse

}