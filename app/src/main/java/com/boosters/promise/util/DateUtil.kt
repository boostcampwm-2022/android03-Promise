package com.boosters.promise.util

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateUtil {

    private const val DATE_FORMAT = "yyyy/MM/dd HH:mm"
    private val dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)

    fun getDelayMillisFromCurrentTime(dateString: String): Long {
        return dateStringToMillis(dateString) * 1000 - System.currentTimeMillis()
    }

    private fun dateStringToMillis(dateString: String): Long {
        val time = LocalDateTime.parse(dateString, dateFormatter)
        return time.toEpochSecond(ZoneOffset.UTC)
    }

}