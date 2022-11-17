package com.boosters.promise.data.promise

interface PromiseRepository {

    fun addPromise(promise: Promise)

    fun removePromise(promise: Promise)

    suspend fun getPromiseList(date: String): MutableList<Promise>

}