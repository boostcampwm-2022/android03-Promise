package com.boosters.promise.data.promise.source

import com.boosters.promise.data.promise.PromiseBody

interface PromiseRemoteDataSource {

    fun addPromise(promise: PromiseBody)

    fun removePromise(promise: PromiseBody)

    suspend fun getPromiseList(date: String): MutableList<PromiseBody>

}