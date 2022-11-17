package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.source.PromiseRemoteDataSource
import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
) : PromiseRepository {

    override fun addPromise(promise: Promise) {
        promiseRemoteDataSource.addPromise(promise)
    }

    override fun removePromise(promise: Promise) {
        promiseRemoteDataSource.removePromise(promise)
    }

    override suspend fun getPromiseList(date: String): MutableList<Promise> {
        return promiseRemoteDataSource.getPromiseList(date)
    }

}