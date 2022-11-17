package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.Promise

interface PromiseRepository {

    fun addPromise(promise: Promise)

    fun removePromise(promise: Promise)

    suspend fun getPromiseList(date: String): MutableList<Promise>

}