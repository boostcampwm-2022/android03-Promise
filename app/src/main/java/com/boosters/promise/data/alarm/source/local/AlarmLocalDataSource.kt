package com.boosters.promise.data.alarm.source.local

interface AlarmLocalDataSource {

    suspend fun getAlarm(promiseId: String): Result<AlarmEntity>

    suspend fun getAlarms(): List<AlarmEntity>

    suspend fun getAlarmCount(): Int

    suspend fun insertAlarm(alarm: AlarmEntity): Long

    suspend fun deleteAlarm(promiseId: String): Int

}