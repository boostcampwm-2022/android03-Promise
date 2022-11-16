package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.source.PromiseRemoteDataSource
import com.boosters.promise.data.promise.source.local.PromiseLocalDataSource
import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
    private val promiseLocalDataSource: PromiseLocalDataSource
) : PromiseRepository {

    override suspend fun addPromise(promise: Promise) {
        val result = promiseRemoteDataSource.addPromise(promise)
        if (result != null) {
            promiseLocalDataSource.addPromise(result)
        }
    }

    override suspend fun removePromise(promise: Promise) {
        val isSuccessful = promiseRemoteDataSource.removePromise(promise)
        if (isSuccessful) {
            promiseLocalDataSource.removePromise(promise)
        }
    }

    override suspend fun getPromiseList(date: String): List<Promise> {
        return promiseLocalDataSource.getPromiseList(date)
    }

}