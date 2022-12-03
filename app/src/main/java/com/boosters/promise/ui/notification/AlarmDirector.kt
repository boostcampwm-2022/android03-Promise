package com.boosters.promise.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.boosters.promise.data.promise.Promise
import java.util.*

class AlarmDirector(
    private val context: Context
) {

    private val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

    fun registerAlarm(promise: Promise) {
        val date = promise.date.split(DATE_SPLIT).map { it.toInt() }
        val time = promise.time.split(TIME_SPLIT).map { it.toInt() }

        val cal = transferToCalendar(date, time)
        if (Calendar.getInstance() >= cal) {
            return
        }
        val dis = SystemClock.elapsedRealtime() + (cal.timeInMillis - Calendar.getInstance().timeInMillis)
        val requestCode = promise.promiseId.hashCode()
        Log.d("MainActivity", "등록 ${requestCode}")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            dis,
            getPendingIntent(promise, requestCode)
        )
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
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            promiseId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("MainActivity", "삭제 ${promiseId.hashCode()}")
        alarmManager.cancel(pendingIntent)
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