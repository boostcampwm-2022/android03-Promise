package com.boosters.promise.data.network

import com.boosters.promise.data.notification.source.remote.NotificationRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CloudMessagingService {

    @Headers(
        "Content-Type: application/json",
        "Authorization: key=AAAAp3dHyfU:APA91bEOp3rEcmyHyhCvOtdZd3p-Rl6EJqqiNJCLKmscovQThchZwjJ6ksEt_nVHvKjOgQpcSv10dZvmQr1x_-VTKADav2Fub5l6KcqQDUc7lMsX9Q42y_KRuIm2pcL0xA7Ike0C4dBr"
    )
    @POST("fcm/send")
    suspend fun sendNotification(@Body notification: NotificationRequestBody)

}