package com.boosters.promise.data.alarm.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.boosters.promise.data.alarm.source.local.AlarmEntity

@Dao
interface AlarmDao {

    @Query("SELECT * FROM AlarmEntity WHERE promiseId = :promiseId")
    suspend fun getAlarm(promiseId: String): AlarmEntity

    @Query("SELECT COUNT(*) FROM AlarmEntity")
    suspend fun getAlarmCount(): Int

    @Insert(onConflict = REPLACE)
    suspend fun insertAlarm(alarm: AlarmEntity): Long

    @Query("DELETE FROM AlarmEntity WHERE promiseId = :promiseId")
    suspend fun deleteAlarm(promiseId: String): Int

}