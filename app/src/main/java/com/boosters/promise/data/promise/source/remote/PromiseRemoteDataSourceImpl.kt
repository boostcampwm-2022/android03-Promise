package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.source.PromiseRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class PromiseRemoteDataSourceImpl @Inject constructor(
    private val database: FirebaseFirestore
) : PromiseRemoteDataSource {

    private val promiseRef = database.collection(DATABASE_PROMISE_REF_PATH)

    override suspend fun addPromise(promise: Promise) {
        var id = promise.id
        if (promise.id == "") {
            id = promiseRef.document().id
        }
        promiseRef.document(id).set(promise.copy(id = id))
    }

    override suspend fun removePromise(promise: Promise) {
        promiseRef.document(promise.id).delete()
    }

    companion object {
        private const val DATABASE_PROMISE_REF_PATH = "promise"
    }

}