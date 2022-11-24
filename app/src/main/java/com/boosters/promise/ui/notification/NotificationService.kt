package com.boosters.promise.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.boosters.promise.R
import com.boosters.promise.ui.promisesetting.PromiseSettingActivity
import com.boosters.promise.ui.promisesetting.PromiseSettingViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            sendNotification(remoteMessage)
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val uniId = System.currentTimeMillis().toInt()

        val intent = Intent(this, PromiseSettingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CHANNEL_NAME
        }
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(remoteMessage.data[MESSAGE_TITLE].toString())
            .setContentText(String.format(getString(R.string.promiseSetting_notification), remoteMessage.data[MESSAGE_BODY].toString()))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(uniId, builder.build())
    }

    companion object {
        const val CHANNEL_ID = "my_channel"
        const val CHANNEL_NAME = "Notice"
        const val MESSAGE_TITLE = "title"
        const val MESSAGE_BODY = "body"
    }

}