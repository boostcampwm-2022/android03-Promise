package com.boosters.promise.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boosters.promise.data.promise.Promise

@Database(entities = [Promise::class], version = 1)
@TypeConverters(UserTypeConverter::class)
abstract class PromiseDatabase : RoomDatabase() {
    abstract fun promiseItemDao(): PromiseDao
}