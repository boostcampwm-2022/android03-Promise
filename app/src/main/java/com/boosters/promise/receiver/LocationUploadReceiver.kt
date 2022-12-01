package com.boosters.promise.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.boosters.promise.service.locationupload.LocationUploadForegroundService
import com.boosters.promise.util.DateUtil

class LocationUploadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        val promiseId = intent.getStringExtra(PROMISE_ID_KEY)

        when (intent.action) {
            ACTION_LOCATION_UPLOAD_SERVICE_START -> {
                val promiseDateTime = intent.getStringExtra(PROMISE_DATE_TIME_KEY)
                startLocationUploadForegroundService(context, promiseId, promiseDateTime)
            }
            ACTION_LOCATION_UPLOAD_SERVICE_STOP -> stopLocationUploadForegroundService(context, promiseId)
        }
    }

    private fun startLocationUploadForegroundService(context: Context, promiseId: String?, promiseDateTime: String?) {
        val intent = Intent(context, LocationUploadForegroundService::class.java).apply {
            if (promiseId == null || promiseDateTime == null) return
            action = ACTION_LOCATION_UPLOAD_SERVICE_START
            putExtra(LocationUploadForegroundService.ID_KEY, promiseId)
            putExtra(LocationUploadForegroundService.DELAY_UNTIL_END_TIME_KEY, DateUtil.getDelayMillisFromCurrentTime(promiseDateTime))
        }
        context.startForegroundService(intent)
    }

    private fun stopLocationUploadForegroundService(context: Context, promiseId: String?) {
        val intent = Intent(context, LocationUploadForegroundService::class.java).apply {
            action = ACTION_LOCATION_UPLOAD_SERVICE_STOP
            putExtra(LocationUploadForegroundService.ID_KEY, promiseId)
        }
        context.startForegroundService(intent)
    }

    companion object {
        const val ACTION_LOCATION_UPLOAD_SERVICE_START = LocationUploadForegroundService.ACTION_LOCATION_UPLOAD_SERVICE_START
        const val ACTION_LOCATION_UPLOAD_SERVICE_STOP = LocationUploadForegroundService.ACTION_LOCATION_UPLOAD_SERVICE_STOP

        const val PROMISE_ID_KEY = "promiseIdKey"
        const val PROMISE_DATE_TIME_KEY = "promiseDateTimeKey"
    }

}