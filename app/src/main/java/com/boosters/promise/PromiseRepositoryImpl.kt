package com.boosters.promise

import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
    private val promiseLocalDataSource: PromiseLocalDataSource
) : PromiseRepository {

    override suspend fun addPromise(promise: Promise) {
        promiseLocalDataSource.addPromise(promise)
    }

    override suspend fun removePromise(promise: Promise) {
        promiseLocalDataSource.removePromise(promise)
    }

}