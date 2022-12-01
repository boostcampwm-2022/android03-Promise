package com.boosters.promise.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.boosters.promise.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmDirector: AlarmDirector

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            context.startService(Intent(context, BootService::class.java))
        } else {
            val promiseId = intent.getStringExtra(AlarmDirector.PROMISE_ID) ?: return
            val promiseTitle = intent.getStringExtra(AlarmDirector.PROMISE_TITLE)
            val promiseDate = intent.getStringExtra(AlarmDirector.PROMISE_DATE)

            alarmDirector.removeLocalAlarm(promiseId)
            showNotification(context, promiseTitle, promiseDate)
        }

        pendingResult.finish()
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