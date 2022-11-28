package com.boosters.promise.data.location.di

import android.content.Context
import com.boosters.promise.data.location.LocationRepository
import com.boosters.promise.data.location.LocationRepositoryImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    private const val LOCATIONS_PATH = "locations"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LocationCollectionReference

    @LocationCollectionReference
    @Singleton
    @Provides
    fun provideLocationReference(): CollectionReference = Firebase.firestore.collection(LOCATIONS_PATH)

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext appContext: Context): FusedLocationProviderClient =
        getFusedLocationProviderClient(appContext)

    @Provides
    @Singleton
    fun provideLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository = locationRepositoryImpl

}