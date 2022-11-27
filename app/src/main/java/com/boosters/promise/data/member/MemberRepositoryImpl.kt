package com.boosters.promise.data.member

import com.boosters.promise.data.member.source.remote.MemberRemoteDataSource
import com.boosters.promise.data.member.source.remote.toMember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRepositoryImpl @Inject constructor(
    private val memberRemoteDataSource: MemberRemoteDataSource
) : MemberRepository {

    override fun initMember(promiseId: String, userCodes: List<String>): Result<Unit> {
        return memberRemoteDataSource.initMembers(promiseId, userCodes)
    }

    override fun updateIsAcceptLocation(member: Member): Result<Unit> {
        return memberRemoteDataSource.updateIsAcceptLocation(member.toMemberBody())
    }

    override fun getMembers(promiseId: String): Flow<List<Member>> {
        return memberRemoteDataSource.getMembers(promiseId).map { members ->
            members.mapNotNull { memberBody ->
                try {
                    memberBody.toMember()
                } catch (e: NullPointerException) {
                    null
                }
            }
        }
    }

}