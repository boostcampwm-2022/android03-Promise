package com.boosters.promise.data.promise

import android.os.Parcelable
import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.promise.source.remote.PromiseBody
import com.boosters.promise.data.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Promise(
    val promiseId: String = "",
    val title: String = "",
    val destinationName: String = "",
    val destinationGeoLocation: GeoLocation = GeoLocation(),
    val date: String = "",
    val time: String = "",
    val members: List<User> = emptyList()
) : Parcelable

fun Promise.toPromiseBody() =
    PromiseBody(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationGeoLocation = destinationGeoLocation,
        date = date,
        time = time,
        members = members.map { it.userCode }
    )