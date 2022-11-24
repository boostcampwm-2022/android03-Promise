package com.boosters.promise.data.notification.source.remote

import com.boosters.promise.data.promise.Promise

interface NotificationRemoteDataSource {

    suspend fun sendNotification(title: String, message: Promise, token: String, key: String)

}