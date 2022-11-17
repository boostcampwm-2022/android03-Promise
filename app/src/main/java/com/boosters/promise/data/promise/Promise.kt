package com.boosters.promise.data.promise

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.boosters.promise.data.user.User

@Entity(tableName = "promise")
data class Promise(
    @PrimaryKey val id: String,
    val title: String,
    val destination: String,
    val destinationX: Int,
    val destinationY: Int,
    val date: String,
    val time: String,
    val members: List<User>
)