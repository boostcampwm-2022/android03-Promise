package com.boosters.promise.data.notification.source.remote

interface NotificationRemoteDataSource {

    suspend fun sendNotification(title: String, message: String, token: String, key: String)

}