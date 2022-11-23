package com.boosters.promise.data.invite.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.boosters.promise.data.user.User

@Entity
data class UserEntity(
    @PrimaryKey val userCode: String,
    @ColumnInfo(name = "userName") val userName: String
)

fun UserEntity.toUser() = User(
    userCode = userCode,
    userName = userName
)