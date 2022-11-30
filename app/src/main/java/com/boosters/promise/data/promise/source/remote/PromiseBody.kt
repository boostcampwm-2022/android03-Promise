package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.user.User

data class PromiseBody(
    val promiseId: String = "",
    val title: String = "",
    val destinationName: String = "",
    val destinationGeoLocation: GeoLocation = GeoLocation(),
    val date: String = "",
    val time: String = "",
    val members: List<String> = emptyList()
)

fun PromiseBody.toPromise(members: List<User>) =
    Promise(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationGeoLocation = destinationGeoLocation,
        date = date,
        time = time,
        members = members
    )