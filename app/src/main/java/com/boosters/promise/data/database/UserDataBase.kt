package com.boosters.promise.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boosters.promise.data.invite.source.local.UserEntity
import com.boosters.promise.data.invite.source.local.UserDao

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDataBase: RoomDatabase() {

    abstract fun userDao(): UserDao

}