package com.boosters.promise.data.user.source.remote

import com.boosters.promise.data.model.Location
import com.boosters.promise.data.user.User

data class UserBody(
    val userCode: String? = null,
    val userName: String? = null,
    val location: Location? = null
)

fun UserBody.toUser() =
    User(
        userCode = userCode ?: throw NullPointerException(),
        userName = userName ?: "",
        location = location
    )