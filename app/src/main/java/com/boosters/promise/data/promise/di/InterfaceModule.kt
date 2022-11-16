package com.boosters.promise.data.promise.di

import com.boosters.promise.data.promise.source.PromiseRemoteDataSource
import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSourceImpl
import com.boosters.promise.data.promise.source.remote.PromiseRepository
import com.boosters.promise.data.promise.source.remote.PromiseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object InterfaceModule {

    @Provides
    fun providePromiseRepository(promiseRepositoryImpl: PromiseRepositoryImpl): PromiseRepository {
        return promiseRepositoryImpl
    }

    @Provides
    fun providePromiseRemoteDataSource(promiseRemoteDataSourceImpl: PromiseRemoteDataSourceImpl): PromiseRemoteDataSource {
        return promiseRemoteDataSourceImpl
    }

}