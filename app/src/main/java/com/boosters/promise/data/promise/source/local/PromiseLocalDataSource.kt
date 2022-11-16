package com.boosters.promise.data.promise.source.local

import com.boosters.promise.data.promise.Promise

interface PromiseLocalDataSource {

    suspend fun addPromise(promise: Promise)

    suspend fun removePromise(promise: Promise)

    suspend fun getPromiseList(date: String): List<Promise>

}