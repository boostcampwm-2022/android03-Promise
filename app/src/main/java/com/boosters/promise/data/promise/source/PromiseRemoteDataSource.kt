package com.boosters.promise.data.promise.source

import com.boosters.promise.data.promise.Promise

interface PromiseRemoteDataSource {

    suspend fun addPromise(promise: Promise): Promise?

    suspend fun removePromise(promise: Promise): Boolean

}