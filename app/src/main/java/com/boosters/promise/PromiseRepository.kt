package com.boosters.promise

interface PromiseRepository {

    suspend fun addPromise(promise: Promise)

    suspend fun removePromise(promise: Promise)

    suspend fun getPromiseList(date: String): List<Promise>

}