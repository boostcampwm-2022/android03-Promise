package com.boosters.promise.network

import retrofit2.http.GET
import retrofit2.http.Query

interface PromiseService {
    @GET("v1/search/local.json")
    suspend fun searchLocalQuery(
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null
    ): LocalResponse
}