package com.boosters.promise.data.user.source.remote

import com.boosters.promise.data.user.Location

data class UserBody(
    val userCode: String? = null,
    val userName: String? = null,
    val location: Location? = null
)
