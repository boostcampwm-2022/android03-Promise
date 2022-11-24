package com.boosters.promise.data.promise

import com.boosters.promise.data.user.User
import kotlinx.coroutines.flow.Flow

interface PromiseRepository {

    fun addPromise(promise: Promise): Flow<Boolean>

    fun removePromise(promiseId: String): Flow<Boolean>

    suspend fun getPromiseList(myInfo: User, date: String): List<Promise>

}