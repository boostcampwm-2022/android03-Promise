package com.boosters.promise.data.user.source.remote

import com.boosters.promise.data.network.NetworkConnectionUtil
import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSourceImpl
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.di.UserModule
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSourceImpl @Inject constructor(
    @UserModule.UserCollectionReference private val userCollectionReference: CollectionReference,
    private val networkConnectionUtil: NetworkConnectionUtil
) : UserRemoteDataSource {

    override suspend fun requestSignUp(userName: String): Result<UserBody> = runCatching {
        networkConnectionUtil.checkNetworkOnline()

        val userCode = userCollectionReference.document().id.take(USER_CODE_LENGTH)
        val token = FirebaseMessaging.getInstance().token.await()
        val userBody = UserBody(
            userCode = userCode,
            userName = userName,
            location = null,
            token = token
        )
        userCollectionReference.document(userCode).set(
            userBody
        ).await()

        userBody
    }

    override fun getUser(userCode: String): Flow<UserBody> =
        userCollectionReference.document(userCode).snapshots().mapNotNull {
            it.toObject(UserBody::class.java)
        }

    override suspend fun getUserList(userCode: List<String>): List<User> {
        val task = userCollectionReference
            .whereIn(USER_CODE_KEY, userCode)
            .get()
        task.await()
        return task.result.documents.mapNotNull {
            it.toObject(UserBody::class.java)?.toUser()
        }
    }

    companion object {
        private const val USER_CODE_LENGTH = 6
        private const val USER_CODE_KEY = "userCode"
    }

}