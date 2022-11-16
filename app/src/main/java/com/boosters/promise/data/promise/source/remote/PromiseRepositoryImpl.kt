package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.PromiseBody
import com.boosters.promise.data.promise.source.PromiseRemoteDataSource
import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
) : PromiseRepository {

    override fun addPromise(promise: PromiseBody) {
        promiseRemoteDataSource.addPromise(promise)
    }

    override fun removePromise(promise: PromiseBody) {
        promiseRemoteDataSource.removePromise(promise)
    }

    override suspend fun getPromiseList(date: String): MutableList<PromiseBody> {
        return promiseRemoteDataSource.getPromiseList(date)
    }

}