package com.boosters.promise.data.promise

import com.boosters.promise.data.promise.source.local.PromiseLocalDataSource
import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSource
import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
    private val promiseLocalDataSource: PromiseLocalDataSource
) : PromiseRepository {

    override suspend fun addPromise(promise: Promise) {
        promiseRemoteDataSource.addPromise(promise)
    }

    override suspend fun removePromise(promise: Promise) {
        promiseRemoteDataSource.removePromise(promise)
    }

    override suspend fun getPromiseList(date: String): List<Promise> {
        return promiseLocalDataSource.getPromiseList(date)
    }

}