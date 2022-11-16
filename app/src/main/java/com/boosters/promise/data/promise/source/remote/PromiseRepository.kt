package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.PromiseRequestBody
import com.boosters.promise.data.promise.PromiseResponseBody

interface PromiseRepository {

    fun addPromise(promise: PromiseRequestBody)

    fun removePromise(promise: PromiseRequestBody)

    suspend fun getPromiseList(date: String): MutableList<PromiseResponseBody>

}