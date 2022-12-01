package com.boosters.promise.data.notification

import com.boosters.promise.data.notification.source.remote.NotificationRemoteDataSource
import com.boosters.promise.data.promise.Promise
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
) : NotificationRepository {

    override suspend fun sendNotification(
        title: String,
        message: Promise,
        token: String,
    ) {
        notificationRemoteDataSource.sendNotification(title, message, token)
    }

}