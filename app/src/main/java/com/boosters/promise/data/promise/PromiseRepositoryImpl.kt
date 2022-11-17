package com.boosters.promise.data.promise

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
        val promiseList = mutableListOf<Promise>()
        promiseRemoteDataSource.getPromiseList(date).forEach {
            promiseList.add(it)
        }
        return promiseList
    }

}