package com.boosters.promise.data.network

import com.boosters.promise.data.notification.source.remote.NotificationRequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface CloudMessagingService {

    @POST("fcm/send")
    suspend fun sendNotification(
        @Body notification: NotificationRequestBody
    ): Result<ResponseBody>

}