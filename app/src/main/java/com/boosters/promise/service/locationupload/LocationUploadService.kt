package com.boosters.promise.service.locationupload

interface LocationUploadService {

    fun stopService()

    fun setServiceEndTime(delayMillis: Long)

    fun getServiceEndTime(): Long?

}