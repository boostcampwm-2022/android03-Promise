package com.boosters.promise.data.notification.source.remote

import android.util.Log
import com.boosters.promise.data.network.CloudMessagingService
import javax.inject.Inject

class NotificationRemoteDateSourceImpl @Inject constructor(
    private val cloudMessagingService: CloudMessagingService
) : NotificationRemoteDataSource {

    override suspend fun sendNotification(title: String, message: String, token: String)  {
        Log.d("MainActivity", "{$token}")
        val notification = NotificationRequestBody(token, NotificationRequestData(title, message))
        val result = cloudMessagingService.sendNotification(notification)
        Log.d("MainActivity", "$result")
    }

}