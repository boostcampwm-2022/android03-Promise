package com.boosters.promise.data.user

import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.invite.source.local.UserEntity
import com.boosters.promise.ui.invite.model.UserUiState

data class User(
    val userCode: String = "",
    val userName: String = "",
    val geoLocation: GeoLocation? = null,
    val userToken: String = ""
)

fun User.toUserUiState() =
    UserUiState(
        userName = userName,
        userCode = userCode,
        userToken = userToken
    )

fun User.toUserEntity() =
    UserEntity(
        userName = userName,
        userCode = userCode
    )