package com.boosters.promise

import androidx.room.*

@Dao
interface PromiseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(promise: Promise)

    @Delete
    suspend fun delete(promise: Promise)

    @Query("SELECT * FROM promise")
    suspend fun getAll(): List<Promise>

    @Query("DELETE FROM promise")
    suspend fun deleteAll()

}