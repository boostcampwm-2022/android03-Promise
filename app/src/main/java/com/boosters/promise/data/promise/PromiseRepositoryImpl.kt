package com.boosters.promise.data.promise

import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSource
import com.boosters.promise.data.promise.source.remote.toPromise
import com.boosters.promise.data.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
) : PromiseRepository {

    override fun addPromise(promise: Promise): Flow<Boolean> {
        return promiseRemoteDataSource.addPromise(promise.toPromiseBody())
    }

    override fun removePromise(promiseId: String): Flow<Boolean> {
        return promiseRemoteDataSource.removePromise(promiseId)
    }

    override fun getPromise(promiseId: String): Flow<Promise> {
        return promiseRemoteDataSource.getPromise(promiseId).mapNotNull { it.toPromise() }
    }

    override fun getPromiseList(user: User): Flow<List<Promise>> {
        return promiseRemoteDataSource.getPromiseList(user).mapNotNull { promiseList ->
            try {
                promiseList.map { it.toPromise() }
            } catch (e: NullPointerException) {
                null
            }
        }
    }

}