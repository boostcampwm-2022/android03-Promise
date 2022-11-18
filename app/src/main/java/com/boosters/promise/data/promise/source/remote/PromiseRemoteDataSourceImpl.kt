package com.boosters.promise.data.promise.source.remote

import android.util.Log
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseBody
import com.boosters.promise.data.promise.toPromise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PromiseRemoteDataSourceImpl @Inject constructor(
    private val database: FirebaseFirestore
) : PromiseRemoteDataSource {

    private val promiseRef = database.collection(DATABASE_PROMISE_REF_PATH)

    override fun addPromise(promise: Promise): Flow<Boolean> {
        var id = promise.promiseId
        if (promise.promiseId == "") {
            id = promiseRef.document().id
        }
        return callbackFlow {
            promiseRef.document(id).set(promise.copy(promiseId = id))
                .addOnSuccessListener {
                    trySend(true)
                }
                .addOnFailureListener {
                    trySend(false)
                    close()
                }
            awaitClose {
                cancel()
            }
        }
    }

    override fun removePromise(promise: Promise) {
        promiseRef.document(promise.promiseId).delete()
    }

    override suspend fun getPromiseList(date: String): MutableList<Promise> {
        val promiseList = mutableListOf<Promise>()
        val task = promiseRef.whereEqualTo("date", date).get()
        task.await()
        task.result.documents.forEach {
            it.toObject(PromiseBody::class.java)
                ?.let { promiseBody -> promiseList.add(promiseBody.toPromise()) }
        }
        return promiseList
    }

    companion object {
        private const val DATABASE_PROMISE_REF_PATH = "promise"
    }

}