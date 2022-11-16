package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.PromiseRequestBody
import com.boosters.promise.data.promise.PromiseResponseBody
import com.boosters.promise.data.promise.source.PromiseRemoteDataSource
import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
) : PromiseRepository {

    override fun addPromise(promise: PromiseRequestBody) {
        promiseRemoteDataSource.addPromise(promise)
    }

    override fun removePromise(promise: PromiseRequestBody) {
        promiseRemoteDataSource.removePromise(promise)
    }

    override suspend fun getPromiseList(date: String): MutableList<PromiseResponseBody> {
        return promiseRemoteDataSource.getPromiseList(date)
    }

}