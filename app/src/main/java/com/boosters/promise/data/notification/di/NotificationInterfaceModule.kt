package com.boosters.promise.data.notification.di

import com.boosters.promise.data.notification.NotificationRepository
import com.boosters.promise.data.notification.NotificationRepositoryImpl
import com.boosters.promise.data.notification.source.remote.NotificationRemoteDataSource
import com.boosters.promise.data.notification.source.remote.NotificationRemoteDateSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationInterfaceModule {

    @Singleton
    @Provides
    fun provideNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository {
        return notificationRepositoryImpl
    }

    @Singleton
    @Provides
    fun provideNotificationRemoteDataSource(notificationRemoteDateSourceImpl: NotificationRemoteDateSourceImpl): NotificationRemoteDataSource {
        return notificationRemoteDateSourceImpl
    }

}