package com.boosters.promise.data.promise.source

import com.boosters.promise.data.promise.PromiseRequestBody
import com.boosters.promise.data.promise.PromiseResponseBody

interface PromiseRemoteDataSource {

    fun addPromise(promise: PromiseRequestBody)

    fun removePromise(promise: PromiseRequestBody)

    suspend fun getPromiseList(date: String): MutableList<PromiseResponseBody>

}