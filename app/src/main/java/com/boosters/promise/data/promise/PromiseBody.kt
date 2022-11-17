package com.boosters.promise.data.promise

data class PromiseBody(
    val promiseId: String = "",
    val title: String = "",
    val destinationName: String = "",
    val destinationLocation: Location? = null,
    val date: String = "",
    val time: String = "",
    val members: List<String> = listOf()
)

fun PromiseBody.toPromise() =
    Promise(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationLocation = destinationLocation,
        date = date,
        time = time,
        members = members
    )