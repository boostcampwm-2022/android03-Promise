package com.boosters.promise.data.alarm

import com.boosters.promise.data.alarm.source.local.AlarmLocalDataSource
import com.boosters.promise.data.alarm.source.local.toAlarm
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmLocalDataSource: AlarmLocalDataSource
) : AlarmRepository {

    override suspend fun getAlarm(promiseId: String): Result<Alarm> {
        return alarmLocalDataSource.getAlarm(promiseId).mapCatching { it.toAlarm() }
    }

    override suspend fun getAlarms(): List<Alarm> {
        return alarmLocalDataSource.getAlarms().map { it.toAlarm() }
    }

    override suspend fun getAlarmCount(): Int {
        return alarmLocalDataSource.getAlarmCount()
    }

    override suspend fun addAlarm(alarm: Alarm): Long {
        return alarmLocalDataSource.insertAlarm(alarm.toAlarmEntity())
    }

    override suspend fun deleteAlarm(promiseId: String): Int {
        return alarmLocalDataSource.deleteAlarm(promiseId)
    }

}