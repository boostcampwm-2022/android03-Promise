package com.boosters.promise.data.alarm

interface AlarmRepository {

    suspend fun getAlarm(promiseId: String): Result<Alarm>

    suspend fun getAlarms(): List<Alarm>

    suspend fun addAlarm(alarm: Alarm)

    suspend fun deleteAlarm(promiseId: String)

    suspend fun deleteAll()

}