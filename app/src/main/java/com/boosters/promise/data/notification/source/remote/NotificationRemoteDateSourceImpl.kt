package com.boosters.promise.data.notification.source.remote

import com.boosters.promise.data.network.CloudMessagingService
import javax.inject.Inject

class NotificationRemoteDateSourceImpl @Inject constructor(
    private val cloudMessagingService: CloudMessagingService
) : NotificationRemoteDataSource {

    override suspend fun sendNotification(notificationRequestBody: NotificationRequestBody)  {
        cloudMessagingService.sendNotification(notificationRequestBody)
    }

}