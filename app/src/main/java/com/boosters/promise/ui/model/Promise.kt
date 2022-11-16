package com.boosters.promise.ui.model

import com.boosters.promise.data.promise.Location
import com.boosters.promise.data.promise.PromiseRequestBody

data class Promise(
    val promiseId: String = "",
    val title: String = "",
    val destinationName: String = "",
    val destinationLocation: Location? = null,
    val date: String = "",
    val time: String = "",
    val members: List<String>? = null
)

fun Promise.toPromiseRequestBody() =
    PromiseRequestBody(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationLocation = destinationLocation,
        date = date,
        time = time,
        members = members
    )