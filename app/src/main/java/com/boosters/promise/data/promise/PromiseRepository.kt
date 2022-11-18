package com.boosters.promise.data.promise

import kotlinx.coroutines.flow.Flow

interface PromiseRepository {

    fun addPromise(promise: Promise): Flow<Boolean>

    fun removePromise(promise: Promise)

    suspend fun getPromiseList(date: String): MutableList<Promise>

}