package com.boosters.promise.data.member

import kotlinx.coroutines.flow.Flow

interface MemberRepository {

    fun initMember(promiseId: String, userCodes: List<String>): Result<Unit>

    fun getMembers(promiseId: String): Flow<List<Member>>

    fun removeMember(promiseId: String): Result<Unit>

    fun removeMember(promiseId: String, userCode: String): Result<Unit>

    suspend fun addIsAcceptLocation(promiseId: String)

    suspend fun updateIsAcceptLocation(member: Member): Result<Unit>

    suspend fun removeIsAcceptLocation(promiseId: String)

    fun getIsAcceptLocation(promiseId: String): Flow<Result<Boolean>>

}