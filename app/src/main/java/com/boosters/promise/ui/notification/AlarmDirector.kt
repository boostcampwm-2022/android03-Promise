package com.boosters.promise.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import com.boosters.promise.data.alarm.Alarm
import com.boosters.promise.data.alarm.AlarmRepository
import com.boosters.promise.data.promise.Promise
import kotlinx.coroutines.*
import java.util.*

class AlarmDirector(
    private val context: Context,
    private val alarmRepository: AlarmRepository
) {

    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }
    private val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

    fun registerAlarm(promise: Promise) {
        val date = promise.date.split("/").map { it.toInt() }
        val time = promise.time.split(":").map { it.toInt() }
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, date[0])
        cal.set(Calendar.MONTH, date[1] - 1)
        cal.set(Calendar.DAY_OF_MONTH, date[2])
        cal.set(Calendar.HOUR_OF_DAY, time[0])
        cal.set(Calendar.MINUTE, time[1])

        if (Calendar.getInstance() >= cal) {
            coroutineScope.launch {
                async {
                    alarmRepository.getAlarm(promise.promiseId)
                }.await().onSuccess {
                    alarmRepository.deleteAlarm(promise.promiseId)
                }
                cancel()
            }
            return
        }

        val requestCode = (date.joinToString("").substring(3) + time.joinToString("")).toInt()
        coroutineScope.launch {
            launch {
                alarmRepository.addAlarm(Alarm(
                    promise.promiseId,
                    requestCode,
                    promise.title,
                    promise.date,
                    promise.time
                ))
                cancel()
            }
        }

        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra("promiseId", promise.promiseId)
            .putExtra("promiseTitle", promise.title)
            .putExtra("promiseDate", promise.date)

        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis - ONE_HOUR_IN_Millis,
            pendingIntent
        )
    }

    fun removeAlarm(promiseId: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        coroutineScope.launch {
            async {
                alarmRepository.getAlarm(promiseId)
            }.await().onSuccess { alarm ->
                val pendingIntent = PendingIntent.getBroadcast(context, alarm.requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
                alarmManager.cancel(pendingIntent)
                alarmRepository.deleteAlarm(promiseId)
            }
            cancel()
        }
    }

    fun updateAlarm(promise: Promise) {
        val intent = Intent(context, AlarmReceiver::class.java)
        coroutineScope.launch {
            async {
                alarmRepository.getAlarm(promise.promiseId)
            }.await().onSuccess { alarm ->
                val pendingIntent = PendingIntent.getBroadcast(context, alarm.requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
                alarmManager.cancel(pendingIntent)
                registerAlarm(promise)
            }
            cancel()
        }
    }

    companion object {
        private const val ONE_HOUR_IN_Millis = 3600000
    }

}