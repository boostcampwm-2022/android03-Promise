package com.boosters.promise.data.alarm

import com.boosters.promise.data.alarm.source.local.AlarmEntity

data class Alarm(
    val promiseId: String,
    val requestCode: Int,
    val promiseTitle: String,
    val promiseDate: String,
    val promiseTime: String
)

fun Alarm.toAlarmEntity() = AlarmEntity(
    promiseId = promiseId,
    requestCode = requestCode,
    promiseTitle = promiseTitle,
    promiseDate = promiseDate,
    promiseTime = promiseTime
)
