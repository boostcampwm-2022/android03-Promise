package com.boosters.promise.data.user

data class User(
    val userCode: String,
    val userName: String,
    val location: Location? = null
)
