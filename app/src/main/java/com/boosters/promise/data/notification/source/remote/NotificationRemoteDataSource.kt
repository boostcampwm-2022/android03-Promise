package com.boosters.promise.data.notification.source.remote

interface NotificationRemoteDataSource {

    suspend fun sendNotification(notificationRequestBody: NotificationRequestBody)

}