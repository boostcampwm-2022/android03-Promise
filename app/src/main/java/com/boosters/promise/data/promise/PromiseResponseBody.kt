package com.boosters.promise.data.promise

import com.boosters.promise.ui.model.PromiseState

data class PromiseResponseBody(
    val promiseId: String = "",
    val title: String = "",
    val destinationName: String = "",
    val destinationLocation: Location? = null,
    val date: String = "",
    val time: String = "",
    val members: List<String>? = null
)

fun PromiseResponseBody.PromiseState() =
    PromiseState(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationLocation = destinationLocation,
        date = date,
        time = time,
        members = members
    )