package com.boosters.promise.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boosters.promise.data.alarm.source.local.AlarmDao
import com.boosters.promise.data.alarm.source.local.AlarmEntity

@Database(entities = [AlarmEntity::class], version = 1)
abstract class AlarmDataBase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

}