package com.boosters.promise.data.promise

import com.boosters.promise.data.user.User
import kotlinx.coroutines.flow.Flow

interface PromiseRepository {

    fun addPromise(promise: Promise): Flow<Boolean>

    fun removePromise(promiseId: String): Flow<Boolean>

    fun getPromise(promiseId: String): Flow<Promise>

    fun getPromiseList(user: User, date: String): Flow<List<Promise>>

}