package com.boosters.promise.data.notification

import com.boosters.promise.data.promise.Promise

interface NotificationRepository {

    suspend fun sendNotification(title: String, message: Promise, token: String, key: String)

}