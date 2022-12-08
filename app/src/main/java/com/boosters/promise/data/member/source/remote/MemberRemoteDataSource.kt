package com.boosters.promise.data.member.source.remote

import com.boosters.promise.data.member.di.MemberModule
import com.boosters.promise.data.network.NetworkConnectionUtil
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRemoteDataSource @Inject constructor(
    @MemberModule.MemberCollectionReference private val memberCollectionReference: CollectionReference,
    private val networkConnectionUtil: NetworkConnectionUtil
) {

    fun initMembers(promiseId: String, userCodes: List<String>): Result<Unit> = runCatching {
        networkConnectionUtil.checkNetworkOnline()

        userCodes.forEach { userCode ->
            memberCollectionReference.document().set(
                MemberBody(promiseId, userCode)
            )
        }
    }

    fun updateIsAcceptLocation(memberBody: MemberBody): Result<Unit> = runCatching {
        networkConnectionUtil.checkNetworkOnline()

        memberCollectionReference
            .whereEqualTo(PROMISE_ID_KEY, memberBody.promiseId)
            .whereEqualTo(USER_CODE_KEY, memberBody.userCode)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    memberCollectionReference.document(document.id)
                        .update(IS_ACCEPT_LOCATION_KEY, memberBody.acceptLocation)
                }
            }
    }

    fun getMembers(promiseId: String): Flow<List<MemberBody>> =
        memberCollectionReference.whereEqualTo(PROMISE_ID_KEY, promiseId).snapshots().mapNotNull {
            it.toObjects(MemberBody::class.java)
        }

    fun removeMember(promiseId: String): Result<Unit> = runCatching {
        memberCollectionReference
            .whereEqualTo(PROMISE_ID_KEY, promiseId)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    memberCollectionReference.document(it.id).delete()
                }
            }
    }

    fun removeMember(promiseId: String, userCode: String): Result<Unit> = runCatching {
        memberCollectionReference
            .whereEqualTo(PROMISE_ID_KEY, promiseId)
            .whereEqualTo(USER_CODE_KEY, userCode)
            .get()
            .addOnSuccessListener { documents ->
                memberCollectionReference.document(documents.first().id).delete()
            }
    }

    private companion object {
        const val PROMISE_ID_KEY = "promiseId"
        const val USER_CODE_KEY = "userCode"
        const val IS_ACCEPT_LOCATION_KEY = "acceptLocation"
    }

}