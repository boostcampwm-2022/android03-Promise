package com.boosters.promise.data.notification.source.remote

import com.boosters.promise.data.network.CloudMessagingService
import com.boosters.promise.data.promise.Promise
import javax.inject.Inject

class NotificationRemoteDateSourceImpl @Inject constructor(
    private val cloudMessagingService: CloudMessagingService
) : NotificationRemoteDataSource {

    override suspend fun sendNotification(
        title: String,
        message: Promise,
        token: String,
    ) {
        val notification =
            NotificationRequestBody(token, data = NotificationRequestData(title, message))
        try {
            cloudMessagingService.sendNotification(notification)
        } catch (e: Exception) {
            return
        }
    }

}