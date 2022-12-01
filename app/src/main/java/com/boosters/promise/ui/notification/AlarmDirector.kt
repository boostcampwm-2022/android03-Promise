package com.boosters.promise.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import com.boosters.promise.data.alarm.Alarm
import com.boosters.promise.data.alarm.AlarmRepository
import com.boosters.promise.data.promise.Promise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AlarmDirector(
    private val context: Context,
    private val alarmRepository: AlarmRepository
) {

    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }
    private val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

    fun registerAlarm(promise: Promise) {
        val date = promise.date.split(DATE_SPLIT).map { it.toInt() }
        val time = promise.time.split(TIME_SPLIT).map { it.toInt() }

        val cal = transferToCalendar(date, time)
        if (Calendar.getInstance() >= cal) {
            coroutineScope.launch {
                alarmRepository.getAlarm(promise.promiseId).onSuccess {
                    alarmRepository.deleteAlarm(promise.promiseId)
                }
            }
            return
        }

        val requestCode = promise.promiseId.hashCode()
        addAlarm(promise, requestCode)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis - ONE_HOUR_IN_MILLIS,
            getPendingIntent(promise, requestCode)
        )
    }

    private fun addAlarm(promise: Promise, requestCode: Int) {
        coroutineScope.launch {
            alarmRepository.addAlarm(
                Alarm(
                    promise.promiseId,
                    requestCode,
                    promise.title,
                    promise.date,
                    promise.time
                )
            )
        }
    }

    private fun getPendingIntent(promise: Promise, requestCode: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra(PROMISE_ID, promise.promiseId)
            .putExtra(PROMISE_TITLE, promise.title)
            .putExtra(PROMISE_DATE, promise.date)

        return PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun removeAlarm(promiseId: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        coroutineScope.launch {
            alarmRepository.getAlarm(promiseId).onSuccess { alarm ->
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarm.requestCode,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)
                alarmRepository.deleteAlarm(promiseId)
            }
        }
    }

    fun updateAlarm(promise: Promise) {
        val intent = Intent(context, AlarmReceiver::class.java)
        coroutineScope.launch {
            alarmRepository.getAlarm(promise.promiseId).onSuccess { alarm ->
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarm.requestCode,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)
                registerAlarm(promise)
            }
        }
    }

    fun removeLocalAllAlarm() {
        coroutineScope.launch {
            alarmRepository.deleteAll()
        }
    }

    fun removeLocalAlarm(promiseId: String) {
        coroutineScope.launch {
            alarmRepository.deleteAlarm(promiseId)
        }
    }

    private fun transferToCalendar(date: List<Int>, time: List<Int>): Calendar {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, date[0])
        cal.set(Calendar.MONTH, date[1] - 1)
        cal.set(Calendar.DAY_OF_MONTH, date[2])
        cal.set(Calendar.HOUR_OF_DAY, time[0])
        cal.set(Calendar.MINUTE, time[1])

        return cal
    }

    companion object {
        private const val ONE_HOUR_IN_MILLIS = 3_600_000
        private const val DATE_SPLIT = "/"
        private const val TIME_SPLIT = ":"
        const val PROMISE_ID = "promiseId"
        const val PROMISE_TITLE = "promiseTitle"
        const val PROMISE_DATE = "promiseDate"
    }

}