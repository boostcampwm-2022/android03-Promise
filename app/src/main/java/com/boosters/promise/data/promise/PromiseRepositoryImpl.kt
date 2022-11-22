package com.boosters.promise.data.promise

import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSource
import com.boosters.promise.data.promise.source.remote.toPromise
import com.boosters.promise.data.user.User
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

    override suspend fun getPromiseList(myInfo: User, date: String): List<Promise> {
        return promiseRemoteDataSource.getPromiseList(myInfo, date).map {
            it.toPromise()
        }
    }

}