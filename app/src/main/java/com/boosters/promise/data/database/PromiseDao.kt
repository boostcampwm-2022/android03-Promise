package com.boosters.promise.data.database

import androidx.room.*
import com.boosters.promise.data.promise.Promise

@Dao
interface PromiseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(promise: Promise)

    @Delete
    suspend fun delete(promise: Promise)

    @Query("SELECT * FROM promise WHERE date = :date")
    suspend fun getPromiseList(date: String): List<Promise>

    @Query("DELETE FROM promise")
    suspend fun deleteAll()

}