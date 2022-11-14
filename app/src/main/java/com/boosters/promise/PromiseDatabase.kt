package com.boosters.promise

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Promise::class], version = 1)
@TypeConverters(UserTypeConverter::class)
abstract class PromiseDatabase : RoomDatabase() {
    abstract fun promiseItemDao(): PromiseDao
}