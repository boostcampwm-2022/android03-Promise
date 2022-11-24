package com.boosters.promise.data.friend.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.boosters.promise.data.user.User

@Entity
data class FriendEntity(
    @PrimaryKey val userCode: String,
    @ColumnInfo(name = "userName") val userName: String
)

fun FriendEntity.toUser() = User(
    userCode = userCode,
    userName = userName
)