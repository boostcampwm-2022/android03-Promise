package com.boosters.promise.ui.invite.model

import android.os.Parcelable
import com.boosters.promise.data.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserUiState(
    val userName: String,
    val userCode: String,
    val userToken: String
) : Parcelable

fun UserUiState.toUser() =
    User(
        userName = userName,
        userCode = userCode,
        userToken = userToken
    )
