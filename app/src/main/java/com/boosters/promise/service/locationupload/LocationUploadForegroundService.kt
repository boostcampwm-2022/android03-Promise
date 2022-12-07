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
class LocationUploadForegroundService : LifecycleService() {

    @Inject
    lateinit var locationRepository: LocationRepository

    private val alarmHandler = Handler(HandlerThread(ALARM_HANDLER_NAME).apply { start() }.looper)
    private val uploadRequests = HashMap<String, Long>()

    private var isUploadStart = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (uploadRequests.isEmpty()) {
            startForeground(FOREGROUND_NOTIFICATION_ID, buildNotification())
        }
        if (intent != null) handleUploadRequest(intent)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmHandler.looper.quit()
        lifecycleScope.launch { locationRepository.resetMyGeoLocation() }
        if (isUploadStart) locationRepository.stopLocationUpdates()
    }

    private fun handleUploadRequest(intent: Intent) {
        val id = intent.getStringExtra(ID_KEY) ?: run {
            if (uploadRequests.isEmpty()) stopSelf()
            return
        }
        val delay = intent.getLongExtra(DELAY_UNTIL_END_TIME_KEY, DEFAULT_SERVICE_END_DELAY)
        collectGarbagePromise()

        when (intent.action) {
            ACTION_LOCATION_UPLOAD_SERVICE_START -> {
                if (id in uploadRequests) {
                    modifyUploadRequest(id, delay)
                } else {
                    if (uploadRequests.isEmpty()) initUploadRequest(id, delay) else putUploadRequest(id, delay)
                }
            }
            ACTION_LOCATION_UPLOAD_SERVICE_STOP -> {
                removeUploadRequest(id)
            }
            else -> return
        }
    }

    private fun setEndSchedule(time: Long) {
        alarmHandler.removeCallbacksAndMessages(SERVICE_END_TOKEN)
        alarmHandler.postAtTime({ stopSelf() }, SERVICE_END_TOKEN, time)
    }

    private fun initUploadRequest(id: String, delay: Long) {
        if (delay < MIN_DELAY) {
            stopSelf()
            return
        }
        startLocationUpload()
        putUploadRequest(id, delay)
    }

    private fun putUploadRequest(id: String, delay: Long) {
        if (delay < MIN_DELAY) return

        val newServiceEndTime = SystemClock.uptimeMillis() + delay
        uploadRequests[id] = newServiceEndTime

        if (id == uploadRequests.maxBy { it.value }.key) {
            setEndSchedule(newServiceEndTime)
        }
    }

    private fun modifyUploadRequest(id: String, delay: Long) {
        uploadRequests.replace(id, SystemClock.uptimeMillis() + delay)

        val maxEndTimeUploadRequest = uploadRequests.maxBy { it.value }
        if (maxEndTimeUploadRequest.value - SystemClock.uptimeMillis() < MIN_DELAY) {
            stopSelf()
            return
        }
        setEndSchedule(maxEndTimeUploadRequest.value)
    }

    private fun removeUploadRequest(id: String) {
        uploadRequests.remove(id)

        val newServiceEndTime = uploadRequests.maxByOrNull { it.value }
        if (newServiceEndTime == null || (newServiceEndTime.value - SystemClock.uptimeMillis()) < MIN_DELAY) {
            stopSelf()
            return
        }
        setEndSchedule(newServiceEndTime.value)
    }

    private fun collectGarbagePromise() {
        val currentUptime = SystemClock.uptimeMillis()
        val garbagePromises = uploadRequests.filter { entry ->
            entry.value < currentUptime
        }
        garbagePromises.forEach { uploadRequests.remove(it.key) }
    }

    private fun startLocationUpload() {
        locationRepository.startLocationUpdates()
        isUploadStart = true

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
        const val ACTION_LOCATION_UPLOAD_SERVICE_START = "com.boosters.promise.action.LOCATION_UPLOAD_SERVICE_START"
        const val ACTION_LOCATION_UPLOAD_SERVICE_STOP = "com.boosters.promise.action.LOCATION_UPLOAD_SERVICE_STOP"

        private const val ALARM_HANDLER_NAME = "alarmHandlerName"
        private const val DEFAULT_SERVICE_END_DELAY = 1800_000L
        private const val MIN_DELAY = 10_000L
        private const val SERVICE_END_TOKEN = "serviceEndToken"

        private const val FOREGROUND_NOTIFICATION_ID = 5138
        private const val FOREGROUND_CHANNEL_ID = "foregroundChannelId"

        const val ID_KEY = "idKey"
        const val DELAY_UNTIL_END_TIME_KEY = "delayUntilEndTimeKey"
    }

}