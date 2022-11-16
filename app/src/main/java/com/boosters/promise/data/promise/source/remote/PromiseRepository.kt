package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.Promise

interface PromiseRepository {

    suspend fun addPromise(promise: Promise)

    suspend fun removePromise(promise: Promise)

    suspend fun getPromiseList(date: String): List<Promise>

}