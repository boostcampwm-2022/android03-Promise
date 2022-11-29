package com.boosters.promise.data.alarm.di

import android.content.Context
import androidx.room.Room
import com.boosters.promise.data.alarm.AlarmRepository
import com.boosters.promise.data.alarm.AlarmRepositoryImpl
import com.boosters.promise.data.alarm.source.local.AlarmDao
import com.boosters.promise.data.alarm.source.local.AlarmLocalDataSource
import com.boosters.promise.data.alarm.source.local.AlarmLocalDataSourceImpl
import com.boosters.promise.data.database.AlarmDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmDataModule {

    const val ALARM = "alarm"

    @Singleton
    @Provides
    fun provideAlarmDao(alarmDataBase: AlarmDataBase): AlarmDao {
        return alarmDataBase.alarmDao()
    }

    @Singleton
    @Provides
    fun provideAlarmDatabase(@ApplicationContext applicationContext: Context): AlarmDataBase {
        return Room.databaseBuilder(
            applicationContext,
            AlarmDataBase::class.java,
            ALARM
        ).build()
    }

    @Singleton
    @Provides
    fun provideAlarmLocalDataSource(alarmDao: AlarmDao): AlarmLocalDataSource {
        return AlarmLocalDataSourceImpl(alarmDao)
    }

    @Singleton
    @Provides
    fun provideAlarmRepository(alarmLocalDataSource: AlarmLocalDataSource): AlarmRepository {
        return AlarmRepositoryImpl(alarmLocalDataSource)
    }

}