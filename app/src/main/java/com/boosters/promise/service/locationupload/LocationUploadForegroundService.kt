package com.boosters.promise.service.locationupload

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.boosters.promise.R
import com.boosters.promise.data.location.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationUploadForegroundService : LifecycleService(), LocationUploadService {

    @Inject
    lateinit var locationRepository: LocationRepository

    private val locationUploadServiceBinder = LocationUploadServiceBinder(this)

    private var isStartForegroundService = false

    private val alarmHandler = Handler(HandlerThread(ALARM_HANDLER_NAME).apply { start() }.looper)
    private var serviceEndTime: Long = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getLongExtra(END_TIME_KEY, DEFAULT_SERVICE_END_DELAY)?.let { endTime ->
            setServiceEndTime(endTime)
        } ?: setServiceEndTime(DEFAULT_SERVICE_END_DELAY)

        if (isStartForegroundService.not()) {
            isStartForegroundService = true
            startForeground(FOREGROUND_NOTIFICATION_ID, buildNotification())
            startLocationUpload()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        if (isStartForegroundService.not()) startForegroundService(intent)
        return locationUploadServiceBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmHandler.looper.quit()
        lifecycleScope.launch { locationRepository.resetMyGeoLocation() }
        locationRepository.stopLocationUpdates()
    }

    override fun stopService() {
        stopSelf()
    }

    override fun setServiceEndTime(delayMillis: Long) {
        if (delayMillis <= 1_000) {
            stopService()
            return
        }

        val newServiceEndTime = SystemClock.uptimeMillis() + delayMillis
        alarmHandler.removeCallbacksAndMessages(SERVICE_END_TOKEN)
        alarmHandler.postAtTime({ stopService() }, SERVICE_END_TOKEN, newServiceEndTime)
        serviceEndTime = newServiceEndTime
    }

    override fun getServiceEndTime(): Long {
        return serviceEndTime
    }

    private fun startLocationUpload() {
        locationRepository.startLocationUpdates()

        lifecycleScope.launch {
            locationRepository.lastGeoLocation.collectLatest { geoLocation ->
                if (geoLocation != null) {
                    locationRepository.uploadMyGeoLocation(geoLocation)
                }
            }
        }
    }

    private fun buildNotification(): Notification {
        createNotificationChannel()

        return Notification.Builder(this, FOREGROUND_CHANNEL_ID)
            .setContentTitle(getText(R.string.locationService_notification_title))
            .setSmallIcon(R.drawable.ic_marker)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        val name = getText(R.string.locationService_notification_channel_name)
        val descriptionText = getString(R.string.locationService_notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(FOREGROUND_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(FOREGROUND_CHANNEL_ID) == null) {
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val ALARM_HANDLER_NAME = "alarmHandlerName"
        private const val DEFAULT_SERVICE_END_DELAY = 60_000L
        private const val SERVICE_END_TOKEN = "serviceEndToken"

        private const val FOREGROUND_NOTIFICATION_ID = 5138
        private const val FOREGROUND_CHANNEL_ID = "foregroundChannelId"

        const val END_TIME_KEY = "endTimeKey"
    }

}