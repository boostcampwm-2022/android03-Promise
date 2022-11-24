package com.boosters.promise.data.notification.source.remote

import com.boosters.promise.data.promise.Promise

data class NotificationRequestBody(
    val to: String,
    val data: NotificationRequestData
)

data class NotificationRequestData(
    val title: String,
    val body: Promise
)