package com.boosters.promise.network

import com.boosters.promise.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    private const val BASE_URL = "https://openapi.naver.com/"

    private val httpHeaderInterceptor = HeaderInterceptor()
    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpHeaderInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    val promiseService: PromiseService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PromiseService::class.java)
}

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Naver-Client-Id", BuildConfig.SEARCH_API_ID)
            .addHeader("X-Naver-Client-Secret", BuildConfig.SEARCH_API_SECRET)
            .build()

        return chain.proceed(request)
    }
}