package com.boosters.promise.data.place.di

import com.boosters.promise.data.network.NaverSearchService
import com.boosters.promise.data.place.PlaceRepository
import com.boosters.promise.data.place.PlaceRepositoryImpl
import com.boosters.promise.data.place.source.remote.PlaceRemoteDataSource
import com.boosters.promise.data.place.source.remote.PlaceRemoteDateSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PlaceModule {

    @Provides
    fun providePlaceRepository(placeRemoteDataSource: PlaceRemoteDataSource): PlaceRepository {
        return PlaceRepositoryImpl(placeRemoteDataSource)
    }

    @Provides
    fun providePlaceRemoteDataSource(naverPlaceService: NaverSearchService): PlaceRemoteDataSource {
        return PlaceRemoteDateSourceImpl(naverPlaceService)
    }

}