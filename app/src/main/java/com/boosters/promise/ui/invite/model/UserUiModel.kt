package com.boosters.promise.ui.invite.model

import android.os.Parcelable
import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserUiModel(
    val userName: String,
    val userCode: String,
    val userToken: String,
    val userLocation: GeoLocation? = null,
    val isSelected: Boolean = false
) : Parcelable

fun UserUiModel.toUser() =
    User(
        userName = userName,
        userCode = userCode,
        userToken = userToken,
        geoLocation = userLocation
    )