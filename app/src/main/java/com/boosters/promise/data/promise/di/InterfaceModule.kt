package com.boosters.promise.data.promise.di

import com.boosters.promise.data.promise.source.PromiseRemoteDataSource
import com.boosters.promise.data.promise.source.local.PromiseLocalDataSource
import com.boosters.promise.data.promise.source.local.PromiseLocalDataSourceImpl
import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSourceImpl
import com.boosters.promise.data.promise.source.remote.PromiseRepository
import com.boosters.promise.data.promise.source.remote.PromiseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InterfaceModule {

    @Provides
    fun providePromiseRepository(promiseRepositoryImpl: PromiseRepositoryImpl): PromiseRepository {
        return promiseRepositoryImpl
    }

    @Provides
    fun providePromiseLocalDataSource(promiseLocalDataSourceImpl: PromiseLocalDataSourceImpl): PromiseLocalDataSource {
        return promiseLocalDataSourceImpl
    }

    @Provides
    fun providePromiseRemoteDataSource(promiseRemoteDataSourceImpl: PromiseRemoteDataSourceImpl): PromiseRemoteDataSource {
        return promiseRemoteDataSourceImpl
    }

}