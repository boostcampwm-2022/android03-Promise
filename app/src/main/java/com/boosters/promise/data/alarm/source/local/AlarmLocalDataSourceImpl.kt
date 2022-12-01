package com.boosters.promise.data.alarm.source.local

import javax.inject.Inject

class AlarmLocalDataSourceImpl @Inject constructor(
    private val alarmDao: AlarmDao
) : AlarmLocalDataSource {

    override suspend fun getAlarm(promiseId: String): Result<AlarmEntity> {
        return runCatching {
            alarmDao.getAlarm(promiseId)
        }
    }

    override suspend fun getAlarms(): List<AlarmEntity> {
        return alarmDao.getAlarms()
    }

    override suspend fun insertAlarm(alarm: AlarmEntity) {
        alarmDao.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(promiseId: String) {
        alarmDao.deleteAlarm(promiseId)
    }

}