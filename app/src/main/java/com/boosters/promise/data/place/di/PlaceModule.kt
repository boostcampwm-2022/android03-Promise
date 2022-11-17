package com.boosters.promise.data.place.di

import com.boosters.promise.data.network.NaverSearchService
import com.boosters.promise.data.place.PlaceRepository
import com.boosters.promise.data.place.PlaceRepositoryImpl
import com.boosters.promise.data.place.source.remote.PlaceRemoteDataSource
import com.boosters.promise.data.place.source.remote.PlaceRemoteDateSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaceModule {

    @Singleton
    @Provides
    fun providePlaceRepository(placeRemoteDataSource: PlaceRemoteDataSource): PlaceRepository {
        return PlaceRepositoryImpl(placeRemoteDataSource)
    }

    @Singleton
    @Provides
    fun providePlaceRemoteDataSource(naverPlaceService: NaverSearchService): PlaceRemoteDataSource {
        return PlaceRemoteDateSourceImpl(naverPlaceService)
    }

}