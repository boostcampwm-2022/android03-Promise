package com.boosters.promise.data.user.source.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await

class UserRemoteDataSourceImpl(
    private val userCollectionReference: CollectionReference
) : UserRemoteDataSource {

    override suspend fun requestSignUp(userName: String): Result<String> = runCatching {
        val userCode = userCollectionReference.document().id.take(USER_CODE_LENGTH)

        userCollectionReference.document(userCode).set(
            UserBody(
                userCode = userCode,
                userName = userName,
                location = null
            )
        ).await()

        userCode
    }

    override fun getUserBody(userCode: String): Flow<UserBody> =
        userCollectionReference.document(userCode).snapshots().mapNotNull {
            it.toObject(UserBody::class.java)
        }

    companion object {
        private const val USER_CODE_LENGTH = 6
    }

}