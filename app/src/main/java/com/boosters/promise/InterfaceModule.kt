package com.boosters.promise

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