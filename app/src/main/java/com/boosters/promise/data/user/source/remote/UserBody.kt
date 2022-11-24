package com.boosters.promise.data.user.source.remote

import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.user.User

data class UserBody(
    val userCode: String? = null,
    val userName: String? = null,
    val geoLocation: GeoLocation? = null,
    val token: String? = null
)

fun UserBody.toUser() =
    User(
        userCode = userCode ?: throw NullPointerException(),
        userName = userName ?: "",
        geoLocation = geoLocation,
        userToken = token ?: ""
    )