package com.boosters.promise.data.user

import android.os.Parcelable
import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.friend.source.local.FriendEntity
import com.boosters.promise.ui.detail.model.MemberUiModel
import com.boosters.promise.ui.invite.model.UserUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userCode: String = "",
    val userName: String = "",
    val geoLocation: GeoLocation? = null,
    val userToken: String = ""
) : Parcelable

fun User.toUserUiModel() =
    UserUiModel(
        userName = userName,
        userCode = userCode,
        userToken = userToken
    )

fun User.toFriendEntity() =
    FriendEntity(
        userName = userName,
        userCode = userCode
    )

fun User.toMemberUiModel() =
    MemberUiModel(
        userCode = userCode,
        userName = userName,
        geoLocation = geoLocation
    )