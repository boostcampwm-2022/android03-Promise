package com.boosters.promise

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promise")
data class Promise(
    @PrimaryKey val id: String,
    val title: String,
    val destination: String,
    val destinationX: Double,
    val destinationY: Double,
    val date: String,
    val time: String,
    val members: List<User>
)