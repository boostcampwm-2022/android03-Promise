package com.boosters.promise.data.promise.source

import com.boosters.promise.data.promise.Promise

interface PromiseRemoteDataSource {

    fun addPromise(promise: Promise)

    fun removePromise(promise: Promise)

    suspend fun getPromiseList(date: String): MutableList<Promise>

}