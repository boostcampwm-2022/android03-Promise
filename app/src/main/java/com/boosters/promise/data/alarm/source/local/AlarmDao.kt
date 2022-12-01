package com.boosters.promise.data.alarm.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface AlarmDao {

    @Query("SELECT * FROM AlarmEntity WHERE promiseId = :promiseId")
    suspend fun getAlarm(promiseId: String): AlarmEntity

    @Query("SELECT * FROM AlarmEntity")
    suspend fun getAlarms(): List<AlarmEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertAlarm(alarm: AlarmEntity)

    @Query("DELETE FROM AlarmEntity WHERE promiseId = :promiseId")
    suspend fun deleteAlarm(promiseId: String)

}