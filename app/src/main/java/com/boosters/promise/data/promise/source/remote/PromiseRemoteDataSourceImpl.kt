package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.data.user.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class PromiseRemoteDataSourceImpl @Inject constructor(
    database: FirebaseFirestore
) : PromiseRemoteDataSource {

    private val promiseRef = database.collection(DATABASE_PROMISE_REF_PATH)

    override fun addPromise(promise: PromiseBody): Flow<Result<String>> {
        var id = promise.promiseId
        if (promise.promiseId == "") {
            id = promiseRef.document().id
        }
        return callbackFlow {
            promiseRef.document(id).set(promise.copy(promiseId = id))
                .addOnSuccessListener {
                    trySend(Result.success(id))
                    close()
                }
                .addOnFailureListener {
                    trySend(Result.failure(it))
                    close()
                }
            awaitClose()
        }
    }

    override fun removePromise(promiseId: String): Flow<Boolean> {
        return callbackFlow {
            promiseRef.document(promiseId).delete()
                .addOnSuccessListener {
                    trySend(true)
                    close()
                }
                .addOnFailureListener {
                    trySend(false)
                    close()
                }
            awaitClose()
        }
    }

    override fun getPromise(promiseId: String): Flow<PromiseBody?> =
        promiseRef.document(promiseId).snapshots().map {
            it.toObject(PromiseBody::class.java)
        }

    override fun getPromiseList(user: User): Flow<List<PromiseBody>> {
        return promiseRef
            .whereArrayContainsAny(DATABASE_PROMISE_MEMBERS_KEY, listOf(user.userCode))
            .snapshots()
            .mapNotNull {
                it.toObjects(PromiseBody::class.java)
            }
    }

    companion object {
        private const val DATABASE_PROMISE_REF_PATH = "promise"
        private const val DATABASE_PROMISE_MEMBERS_KEY = "members"
    }

}