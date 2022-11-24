package com.boosters.promise.data.location.di

import android.content.Context
import com.boosters.promise.data.location.LocationRepository
import com.boosters.promise.data.location.LocationRepositoryImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext appContext: Context): FusedLocationProviderClient =
        getFusedLocationProviderClient(appContext)

    @Provides
    @Singleton
    fun provideLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository = locationRepositoryImpl

}