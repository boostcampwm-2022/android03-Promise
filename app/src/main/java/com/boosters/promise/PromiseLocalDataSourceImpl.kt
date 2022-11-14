package com.boosters.promise

import javax.inject.Inject

class PromiseLocalDataSourceImpl @Inject constructor(
    private val database: PromiseDatabase
) : PromiseLocalDataSource {

    override suspend fun addPromise(promise: Promise) {
        database.promiseItemDao().insert(promise)
    }

    override suspend fun removePromise(promise: Promise) {
        database.promiseItemDao().delete(promise)
    }

}