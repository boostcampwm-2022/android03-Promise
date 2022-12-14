package com.boosters.promise.data.promise

import com.boosters.promise.data.user.User
import kotlinx.coroutines.flow.Flow

interface PromiseRepository {

    suspend fun addPromise(promise: Promise): Flow<Result<String>>

    suspend fun modifyPromise(promise: Promise): Flow<Result<String>>

    fun removePromise(promiseId: String): Flow<Boolean>

    fun getPromise(promiseId: String): Flow<Promise?>

    fun getPromiseList(user: User): Flow<List<Promise>>

}