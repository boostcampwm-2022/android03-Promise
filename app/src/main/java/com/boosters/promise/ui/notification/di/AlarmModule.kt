package com.boosters.promise.ui.notification.di

import android.content.Context
import com.boosters.promise.ui.notification.AlarmDirector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Singleton
    @Provides
    fun provideAlarmDirector(
        @ApplicationContext applicationContext: Context
    ): AlarmDirector {
        return AlarmDirector(applicationContext)
    }

}