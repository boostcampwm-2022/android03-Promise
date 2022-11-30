package com.boosters.promise.ui.notification

import android.app.*
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
        } else {
            val promiseId = intent.getStringExtra("promiseId") ?: return
            val promiseTitle = intent.getStringExtra("promiseTitle")
            val promiseDate = intent.getStringExtra("promiseDate")

            coroutineScope.launch {
                alarmRepository.deleteAlarm(promiseId)
                cancel()
            }

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

        pendingResult.finish()
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