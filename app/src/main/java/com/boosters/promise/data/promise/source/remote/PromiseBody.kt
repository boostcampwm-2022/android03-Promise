package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.model.Location
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.user.User

data class PromiseBody(
    val promiseId: String = "",
    val title: String = "",
    val destinationName: String = "",
    val destinationLocation: Location = Location(),
    val date: String = "",
    val time: String = "",
    val members: List<User> = listOf()
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