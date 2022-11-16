package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.PromiseBody

interface PromiseRepository {

    fun addPromise(promise: PromiseBody)

    fun removePromise(promise: PromiseBody)

    suspend fun getPromiseList(date: String): MutableList<PromiseBody>

}