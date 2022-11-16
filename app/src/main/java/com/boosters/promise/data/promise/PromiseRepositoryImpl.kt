package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.source.PromiseRemoteDataSource
import com.boosters.promise.data.promise.toPromise
import com.boosters.promise.ui.model.Promise
import com.boosters.promise.ui.model.toPromiseRequestBody
import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
) : PromiseRepository {

    override fun addPromise(promise: Promise) {
        promiseRemoteDataSource.addPromise(promise.toPromiseRequestBody())
    }

    override fun removePromise(promise: Promise) {
        promiseRemoteDataSource.removePromise(promise.toPromiseRequestBody())
    }

    override suspend fun getPromiseList(date: String): MutableList<Promise> {
        val promiseList = mutableListOf<Promise>()
        promiseRemoteDataSource.getPromiseList(date).forEach {
            promiseList.add(it.toPromise())
        }
        return promiseList
    }

}