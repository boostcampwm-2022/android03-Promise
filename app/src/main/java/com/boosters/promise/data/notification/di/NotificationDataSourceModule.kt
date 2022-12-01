package com.boosters.promise.data.notification.di

import com.boosters.promise.data.network.CloudMessagingService
import com.boosters.promise.data.network.NotiHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationDataSourceModule {

    private const val BASE_URL = "https://fcm.googleapis.com/"
    private val httpHeaderInterceptor = NotiHeaderInterceptor()
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpHeaderInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideCloudMessagingService(): CloudMessagingService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudMessagingService::class.java)
    }

}