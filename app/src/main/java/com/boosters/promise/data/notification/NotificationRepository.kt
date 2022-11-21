package com.boosters.promise.data.notification

import com.boosters.promise.data.notification.source.remote.NotificationRequestBody

interface NotificationRepository {

    suspend fun sendNotification(notificationRequestBody: NotificationRequestBody)

}