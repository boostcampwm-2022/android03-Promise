package com.boosters.promise.data.alarm.source.local

import javax.inject.Inject

class AlarmLocalDataSourceImpl @Inject constructor(
    private val alarmDao: AlarmDao
) : AlarmLocalDataSource {

    override suspend fun getAlarm(promiseId: String): AlarmEntity {
        return alarmDao.getAlarm(promiseId)
    }

    override suspend fun getAlarmCount(): Int {
        return alarmDao.getAlarmCount()
    }

    override suspend fun insertAlarm(alarm: AlarmEntity): Long {
        return alarmDao.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(promiseId: String): Int {
        return alarmDao.deleteAlarm(promiseId)
    }

}