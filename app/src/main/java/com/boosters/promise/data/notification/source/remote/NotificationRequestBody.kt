package com.boosters.promise.data.notification.source.remote

data class NotificationRequestBody(
    val to: String,
    val priority: String = "high",
    val data: NotificationRequestData
)

data class NotificationRequestData(
    val title: String,
    val body: String
)