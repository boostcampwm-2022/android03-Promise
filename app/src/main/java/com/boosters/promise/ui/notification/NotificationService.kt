package com.boosters.promise.ui.notification

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.boosters.promise.R
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.ui.detail.PromiseDetailActivity
import com.boosters.promise.ui.promisecalendar.PromiseCalendarActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var alarmDirector: AlarmDirector

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            sendNotification(remoteMessage)
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val uniId = System.currentTimeMillis().toInt()
        val promise = Gson().fromJson(remoteMessage.data[MESSAGE_BODY], Promise::class.java)
        val contentText = getContentText(remoteMessage.data[MESSAGE_TITLE], promise)
        val pendingIntent = getPendingIntent(remoteMessage.data[MESSAGE_TITLE], promise.promiseId)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(createChannel())
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(promise.title)
            .setContentText(contentText)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(uniId, builder.build())
    }

    private fun getPendingIntent(title: String?, promiseId: String): PendingIntent? {
        val intent = if (title == NOTIFICATION_DELETE) {
            Intent(this, PromiseCalendarActivity::class.java)
        } else {
            Intent(
                this,
                PromiseDetailActivity::class.java
            ).putExtra(PromiseCalendarActivity.PROMISE_ID_KEY, promiseId)
        }
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return pendingIntent
    }

    private fun getContentText(title: String?, promise: Promise): String {
        return when (title) {
            NOTIFICATION_EDIT -> {
                alarmDirector.registerAlarm(promise)
                String.format(getString(R.string.notification_edit), promise.date)
            }
            NOTIFICATION_ADD -> {
                alarmDirector.registerAlarm(promise)
                String.format(getString(R.string.notification_add), promise.date)
            }
            else -> {
                alarmDirector.removeAlarm(promise.promiseId)
                String.format(getString(R.string.notification_delete), promise.date)
            }
        }
    }

    private fun createChannel(): NotificationChannel {
        return NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CHANNEL_NAME
        }
    }

    companion object {
        const val CHANNEL_ID = "my_channel"
        const val CHANNEL_NAME = "Notice"
        private const val MESSAGE_BODY = "body"
        private const val MESSAGE_TITLE = "title"
        const val NOTIFICATION_EDIT = "0"
        const val NOTIFICATION_ADD = "1"
        const val NOTIFICATION_DELETE = "2"
    }

}