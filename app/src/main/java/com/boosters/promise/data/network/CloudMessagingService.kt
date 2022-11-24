package com.boosters.promise.data.network

import com.boosters.promise.data.notification.source.remote.NotificationRequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface CloudMessagingService {

    @Headers("Content-Type: application/json")
    @POST("fcm/send")
    suspend fun sendNotification(
        @Header("Authorization") key: String,
        @Body notification: NotificationRequestBody
    ): Result<ResponseBody>

}