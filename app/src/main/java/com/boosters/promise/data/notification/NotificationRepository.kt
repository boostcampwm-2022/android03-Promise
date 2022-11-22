package com.boosters.promise.data.notification

interface NotificationRepository {

    suspend fun sendNotification(title: String, message: String, token: String)

}