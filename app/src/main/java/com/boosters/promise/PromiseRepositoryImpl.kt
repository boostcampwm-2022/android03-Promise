package com.boosters.promise

import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
    private val promiseLocalDataSource: PromiseLocalDataSource
) : PromiseRepository {

    override suspend fun addPromise(promise: Promise) {
        promiseLocalDataSource.addPromise(promise)
        promiseRemoteDataSource.addPromise(promise)
    }

    override suspend fun removePromise(promise: Promise) {
        promiseLocalDataSource.removePromise(promise)
        promiseRemoteDataSource.removePromise(promise)
    }

    override suspend fun getPromiseList(date: String): List<Promise> {
        return promiseLocalDataSource.getPromiseList(date)
    }

}