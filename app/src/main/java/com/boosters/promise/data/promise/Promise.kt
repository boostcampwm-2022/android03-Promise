package com.boosters.promise.data.promise

import com.boosters.promise.data.model.Location
import com.boosters.promise.data.user.User

data class Promise(
    val promiseId: String,
    val title: String,
    val destinationName: String,
    val destinationLocation: Location?,
    val date: String,
    val time: String,
    val members: List<User>
)