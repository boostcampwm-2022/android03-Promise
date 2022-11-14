package com.boosters.promise

interface PromiseRemoteDataSource {

    suspend fun addPromise(promise: Promise)

    suspend fun removePromise(promise: Promise)

}