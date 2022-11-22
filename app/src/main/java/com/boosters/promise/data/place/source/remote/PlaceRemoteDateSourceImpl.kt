package com.boosters.promise.data.place.source.remote

import com.boosters.promise.data.network.NaverSearchService
import javax.inject.Inject

class PlaceRemoteDateSourceImpl @Inject constructor(
    private val naverSearchService: NaverSearchService
) : PlaceRemoteDataSource {

    override suspend fun searchPlace(query: String, display: Int) = runCatching {
        naverSearchService.searchPlace(query, display).items.map {
            it?.toPlace()
        }
    }

}