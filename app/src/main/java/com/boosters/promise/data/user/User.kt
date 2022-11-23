package com.boosters.promise.data.user

import android.os.Parcelable
import com.boosters.promise.data.model.Location
import com.boosters.promise.ui.invite.model.UserUiState
import kotlinx.parcelize.Parcelize

data class User(
    val userCode: String = "",
    val userName: String = "",
    val location: Location? = null,
    val userToken: String = ""
)

fun User.toUserUiState() =
    UserUiState(
        userName = userName,
        userCode = userCode,
    )
