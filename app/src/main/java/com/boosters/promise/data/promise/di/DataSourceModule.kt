package com.boosters.promise.data.promise.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    private const val PROMISE_DATABASE_NAME = "Promise_Database"

    @Provides
    @Singleton
    fun providePromiseRemoteDatabase(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

}