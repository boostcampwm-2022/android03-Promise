package com.boosters.promise.data.friend.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface FriendDao {

    @Query("SELECT * FROM FriendEntity ORDER BY userName")
    suspend fun getFriends(): List<FriendEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertFriend(user: FriendEntity)

}