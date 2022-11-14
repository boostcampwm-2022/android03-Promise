package com.boosters.promise

interface PromiseLocalDataSource {

    suspend fun addPromise(promise: Promise)

    suspend fun removePromise(promise: Promise)

    suspend fun updatePromise(promise: Promise)

}