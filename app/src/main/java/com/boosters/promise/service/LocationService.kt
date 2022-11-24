package com.boosters.promise.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.boosters.promise.R
import com.boosters.promise.data.location.LocationRepository
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.start.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : LifecycleService() {

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var userRepository: UserRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycleScope.launch {
            locationRepository.isUploadMyLocation.collectLatest { isUploadMyLocation ->
                if (isUploadMyLocation) {
                    startUploadLocation()
                } else {
                    stopSelf()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startUploadLocation() {
        if (locationRepository.isReceivingLocationUpdates.value.not()) {
            locationRepository.startLocationUpdates()
        }

        showNotification()
        lifecycleScope.launch {
            locationRepository.lastGeoLocation.collectLatest { geoLocation ->
                userRepository.uploadMyGeoLocation(geoLocation)
            }
        }
    }

    private fun showNotification() {
        startForeground(LOCATION_NOTIFICATION_ID, buildNotification())
    }

    private fun buildNotification(): Notification {
        val pendingIntent: PendingIntent =
            Intent(this, StartActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
        createNotificationChannel()

        return Notification.Builder(this, LOCATION_CHANNEL_ID)
            .setContentTitle(getText(R.string.locationService_notification_title))
            .setSmallIcon(R.drawable.ic_marker)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        val name = getText(R.string.locationService_notification_channel_name)
        val descriptionText = getString(R.string.locationService_notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(LOCATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(LOCATION_CHANNEL_ID) == null) {
            notificationManager.createNotificationChannel(channel)
        }
    }

    private companion object {
        val LOCATION_NOTIFICATION_ID = "locationNotification".hashCode()
        const val LOCATION_CHANNEL_ID = "locationChannel"
    }

}