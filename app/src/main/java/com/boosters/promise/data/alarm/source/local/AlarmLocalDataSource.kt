package com.boosters.promise.data.alarm.source.local

interface AlarmLocalDataSource {

    suspend fun getAlarm(promiseId: String): Result<AlarmEntity>

    suspend fun getAlarms(): List<AlarmEntity>

    suspend fun insertAlarm(alarm: AlarmEntity)

    suspend fun deleteAlarm(promiseId: String)

}