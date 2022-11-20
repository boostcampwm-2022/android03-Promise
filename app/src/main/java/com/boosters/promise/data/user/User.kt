package com.boosters.promise.data.user

import com.boosters.promise.ui.invite.model.UserUiState

data class User(
    val userCode: String = "",
    val userName: String = "",
    val location: Location? = null
)

fun User.toUserUiState() =
    UserUiState(
        userName = userName,
        userCode = userCode
    )
