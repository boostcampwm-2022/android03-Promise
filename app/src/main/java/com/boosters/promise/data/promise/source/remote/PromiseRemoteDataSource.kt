package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.user.User
import kotlinx.coroutines.flow.Flow

interface PromiseRemoteDataSource {

    fun addPromise(promise: PromiseBody): Flow<Boolean>

    fun removePromise(promiseId: String): Flow<Boolean>

    suspend fun getPromiseList(user: User, date: String): List<PromiseBody>

}