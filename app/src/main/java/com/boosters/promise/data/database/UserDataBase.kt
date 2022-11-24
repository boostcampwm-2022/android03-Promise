package com.boosters.promise.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boosters.promise.data.friend.source.local.FriendEntity
import com.boosters.promise.data.friend.source.local.FriendDao

@Database(entities = [FriendEntity::class], version = 1)
abstract class UserDataBase : RoomDatabase() {

    abstract fun userDao(): FriendDao

}