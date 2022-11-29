package com.boosters.promise.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import com.boosters.promise.data.alarm.Alarm
import com.boosters.promise.data.alarm.AlarmRepository
import com.boosters.promise.data.promise.Promise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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

        val requestCode = (date.joinToString("").substring(3) + time.joinToString("")).toInt()
        coroutineScope.launch {
            launch {
                alarmRepository.addAlarm(Alarm(
                    promise.promiseId,
                    requestCode
                ))
            }
        }

        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra("promiseId", promise.promiseId)
            .putExtra("promiseTitle", promise.title)
            .putExtra("promiseDate", promise.date)

        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE)

        Log.d("register", "등록 ${requestCode}")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, // 절전 모드일 때도 알람 발생
            cal.timeInMillis,
            pendingIntent
        )
    }

    fun removeAlarm(promiseId: String, isUpdate: Boolean = false) {
        val intent = Intent(context, AlarmReceiver::class.java)
        coroutineScope.launch {
            val requestCode = async {
                alarmRepository.getAlarm(promiseId)
            }.await().requestCode
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pendingIntent)
            Log.d("register", "취소 ${requestCode}")
            if (!isUpdate) {
                alarmRepository.deleteAlarm(promiseId)
            }
        }
    }

}