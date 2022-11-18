package com.boosters.promise.data.promise

import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
) : PromiseRepository {

    override fun addPromise(promise: Promise): Flow<Boolean> {
        return promiseRemoteDataSource.addPromise(promise)
    }

    override fun removePromise(promise: Promise) {
        promiseRemoteDataSource.removePromise(promise)
    }

    override suspend fun getPromiseList(date: String): MutableList<Promise> {
        return promiseRemoteDataSource.getPromiseList(date)
    }

}