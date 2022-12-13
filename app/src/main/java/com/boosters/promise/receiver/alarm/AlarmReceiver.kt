package com.boosters.promise.receiver.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.boosters.promise.R
import com.boosters.promise.service.notification.BootService
import com.boosters.promise.service.notification.NotificationService
import com.boosters.promise.ui.detail.PromiseDetailActivity
import com.boosters.promise.ui.promisecalendar.PromiseCalendarActivity
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

            showNotification(context, promiseTitle, promiseDate, promiseId)
        }

        pendingResult.finish()
    }

    private fun showNotification(
        context: Context,
        promiseTitle: String?,
        promiseDate: String?,
        promiseId: String?
    ) {
        val intent = Intent(
            context,
            PromiseDetailActivity::class.java
        ).putExtra(PromiseCalendarActivity.PROMISE_ID_KEY, promiseId)
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(createChannel())
        val builder = NotificationCompat.Builder(context, NotificationService.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(promiseTitle)
            .setContentIntent(pendingIntent)
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