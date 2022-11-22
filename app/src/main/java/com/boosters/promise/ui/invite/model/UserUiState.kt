package com.boosters.promise.ui.invite.model

import android.os.Parcelable
import com.boosters.promise.data.user.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserUiState(
    val userName: String,
    val userCode: String,
    var isSelected: Boolean = false
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other is UserUiState) {
            return userCode == other.userCode && userName == other.userName
        }
        return false
    }

    override fun hashCode(): Int {
        var result = userName.hashCode()
        result = 31 * result + userCode.hashCode()
        result = 31 * result + isSelected.hashCode()
        return result
    }
}

fun UserUiState.toUser() =
    User(
        userName = userName,
        userCode = userCode
    )
