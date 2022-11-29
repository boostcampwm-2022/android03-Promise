package com.boosters.promise.data.alarm

import com.boosters.promise.data.alarm.source.local.AlarmEntity
import com.boosters.promise.data.friend.source.local.FriendEntity
import com.boosters.promise.data.user.User

data class Alarm(
    val promiseId: String,
    val requestCode: Int
)

fun Alarm.toAlarmEntity() = AlarmEntity(
    promiseId = promiseId,
    requestCode = requestCode
)
