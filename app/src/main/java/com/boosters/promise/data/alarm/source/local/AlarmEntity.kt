package com.boosters.promise.data.alarm.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.boosters.promise.data.alarm.Alarm

@Entity
data class AlarmEntity(
    @PrimaryKey val promiseId: String,
    @ColumnInfo(name = "requestCode") val requestCode: Int,
    @ColumnInfo(name = "promiseTitle") val promiseTitle: String,
    @ColumnInfo(name = "promiseDate") val promiseDate: String,
    @ColumnInfo(name = "promiseTime") val promiseTime: String
)

fun AlarmEntity.toAlarm() = Alarm(
    promiseId = promiseId,
    requestCode = requestCode,
    promiseTitle = promiseTitle,
    promiseDate = promiseDate,
    promiseTime = promiseTime
)

