package com.boosters.promise.data.notification.source.remote

import com.boosters.promise.data.network.CloudMessagingService
import javax.inject.Inject

class NotificationRemoteDateSourceImpl @Inject constructor(
    private val cloudMessagingService: CloudMessagingService
) : NotificationRemoteDataSource {

    override suspend fun sendNotification(title: String, message: String, token: String,  key: String)  {
        val notification = NotificationRequestBody(token, data = NotificationRequestData(title, message))
        try {
            cloudMessagingService.sendNotification(key, notification)
        } catch (e:Exception) {
            return
        }
    }

}