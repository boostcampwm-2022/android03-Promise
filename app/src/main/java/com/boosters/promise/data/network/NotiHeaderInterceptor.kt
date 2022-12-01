package com.boosters.promise.data.network

import com.boosters.promise.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class NotiHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=${BuildConfig.SERVER_KEY}")
                .build()

        return chain.proceed(request)

    }
}