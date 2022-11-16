package com.boosters.promise.data.network

import com.boosters.promise.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Naver-Client-Id", BuildConfig.SEARCH_API_ID)
            .addHeader("X-Naver-Client-Secret", BuildConfig.SEARCH_API_SECRET)
            .build()

        return chain.proceed(request)
    }
}