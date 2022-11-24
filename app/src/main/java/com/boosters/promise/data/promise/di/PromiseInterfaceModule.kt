package com.boosters.promise.data.promise.di

import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.promise.PromiseRepositoryImpl
import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSource
import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PromiseInterfaceModule {

    @Singleton
    @Provides
    fun providePromiseRepository(promiseRepositoryImpl: PromiseRepositoryImpl): PromiseRepository {
        return promiseRepositoryImpl
    }

    @Singleton
    @Provides
    fun providePromiseRemoteDataSource(promiseRemoteDataSourceImpl: PromiseRemoteDataSourceImpl): PromiseRemoteDataSource {
        return promiseRemoteDataSourceImpl
    }

}