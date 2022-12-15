package com.boosters.promise.receiver.alarm

import com.boosters.promise.data.promise.Promise

interface AlarmDirector {

    fun registerAlarm(promise: Promise)

    fun removeAlarm(promiseId: String)

    companion object {
        const val PROMISE_ID = "promiseId"
        const val PROMISE_TITLE = "promiseTitle"
        const val PROMISE_DATE = "promiseDate"
    }

}