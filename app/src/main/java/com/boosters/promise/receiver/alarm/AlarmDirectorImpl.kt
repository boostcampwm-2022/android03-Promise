package com.boosters.promise.receiver.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.boosters.promise.data.promise.Promise
import java.util.*

class AlarmDirectorImpl(
    private val context: Context
) : AlarmDirector {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun registerAlarm(promise: Promise) {
        val date = promise.date.split(DATE_SPLIT).map { it.toInt() }
        val time = promise.time.split(TIME_SPLIT).map { it.toInt() }

        val cal = transferToCalendar(date, time)
        if (Calendar.getInstance() >= cal) {
            return
        }
        val delay =
            SystemClock.elapsedRealtime() + (cal.timeInMillis - Calendar.getInstance().timeInMillis - ONE_HOUR_IN_MILLIS)
        val requestCode = promise.promiseId.hashCode()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            delay,
            getPendingIntent(promise, requestCode)
        )
    }

    private fun getPendingIntent(promise: Promise, requestCode: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra(AlarmDirector.PROMISE_ID, promise.promiseId)
            .putExtra(AlarmDirector.PROMISE_TITLE, promise.title)
            .putExtra(AlarmDirector.PROMISE_DATE, promise.date)

        return PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun removeAlarm(promiseId: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            promiseId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
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
    }

}