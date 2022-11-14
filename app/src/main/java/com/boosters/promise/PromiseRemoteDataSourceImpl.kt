package com.boosters.promise

import javax.inject.Inject

class PromiseRemoteDataSourceImpl @Inject constructor(
    private val database: PromiseDatabase
) : PromiseRemoteDataSource {

    override suspend fun addPromise(promise: Promise) {
        TODO()
    }

    override suspend fun removePromise(promise: Promise) {
        TODO()
    }

}