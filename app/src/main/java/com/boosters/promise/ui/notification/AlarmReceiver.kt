package com.boosters.promise.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.boosters.promise.R
import com.boosters.promise.data.alarm.AlarmRepository
import com.boosters.promise.data.promise.Promise
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var alarmDirector: AlarmDirector
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            callAlarm()
        } else {
            val promiseId = intent.getStringExtra(AlarmDirector.PROMISE_ID) ?: return
            val promiseTitle = intent.getStringExtra(AlarmDirector.PROMISE_TITLE)
            val promiseDate = intent.getStringExtra(AlarmDirector.PROMISE_DATE)

            coroutineScope.launch {
                alarmRepository.deleteAlarm(promiseId)
                cancel()
            }

            showNotification(context, promiseTitle, promiseDate)
        }

        pendingResult.finish()
    }

    private fun callAlarm() {
        coroutineScope.launch {
            val alarms = async {
                alarmRepository.getAlarms()
            }.await()
            alarms.forEach {
                alarmDirector.registerAlarm(
                    Promise(
                        promiseId = it.promiseId,
                        date = it.promiseDate,
                        time = it.promiseTime,
                        title = it.promiseTitle,
                    )
                )
            }
            cancel()
        }
    }

    private fun showNotification(context: Context, promiseTitle: String?, promiseDate: String?) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(createChannel())
        val builder = NotificationCompat.Builder(context, NotificationService.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(promiseTitle)
            .setContentText(
                String.format(
                    context.getString(R.string.notification_request),
                    promiseDate
                )
            )
            .setAutoCancel(true)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun createChannel(): NotificationChannel {
        return NotificationChannel(
            NotificationService.CHANNEL_ID,
            NotificationService.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = NotificationService.CHANNEL_NAME
        }
    }

}