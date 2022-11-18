package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseBody
import kotlinx.coroutines.flow.Flow

interface PromiseRemoteDataSource {

    fun addPromise(promise: Promise): Flow<Boolean>

    fun removePromise(promise: Promise)

    suspend fun getPromiseList(date: String): MutableList<Promise>

}