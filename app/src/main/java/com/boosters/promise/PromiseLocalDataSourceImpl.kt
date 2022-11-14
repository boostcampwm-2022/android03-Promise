package com.boosters.promise

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PromiseLocalDataSourceImpl @Inject constructor(
    private val database: PromiseDatabase
) : PromiseLocalDataSource {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            database.promiseItemDao().deleteAll()
        }
    }

    override suspend fun addPromise(promise: Promise) {
        database.promiseItemDao().insert(promise)
    }

    override suspend fun removePromise(promise: Promise) {
        database.promiseItemDao().delete(promise)
    }

    override suspend fun updatePromise(promise: Promise) {
        database.promiseItemDao().update(promise)
    }

    override suspend fun getPromiseList(date: String): List<Promise> {
        return database.promiseItemDao().getPromiseList(date)
    }

}