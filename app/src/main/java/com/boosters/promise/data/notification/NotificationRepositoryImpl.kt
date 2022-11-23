package com.boosters.promise.data.notification

import com.boosters.promise.data.notification.source.remote.NotificationRemoteDataSource
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
) : NotificationRepository {

    override suspend fun sendNotification(
        title: String,
        message: String,
        token: String,
        key: String
    ) {
        notificationRemoteDataSource.sendNotification(title, message, token, key)
    }

}