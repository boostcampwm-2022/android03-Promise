package com.boosters.promise.data.alarm

interface AlarmRepository {

    suspend fun getAlarm(promiseId: String): Result<Alarm>

    suspend fun getAlarms(): List<Alarm>

    suspend fun getAlarmCount(): Int

    suspend fun addAlarm(alarm: Alarm): Long

    suspend fun deleteAlarm(promiseId: String): Int

}