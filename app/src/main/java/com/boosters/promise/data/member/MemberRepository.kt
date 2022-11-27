package com.boosters.promise.data.member

import kotlinx.coroutines.flow.Flow

interface MemberRepository {

    fun initMember(promiseId: String, userCodes: List<String>): Result<Unit>

    fun updateIsAcceptLocation(member: Member): Result<Unit>

    fun getMembers(promiseId: String): Flow<List<Member>>

}