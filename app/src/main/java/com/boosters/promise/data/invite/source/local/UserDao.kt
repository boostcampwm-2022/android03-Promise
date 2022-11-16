package com.boosters.promise.data.invite.source.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity")
    suspend fun getFriends(): List<UserEntity>

}