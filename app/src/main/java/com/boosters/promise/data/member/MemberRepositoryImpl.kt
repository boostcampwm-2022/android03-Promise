package com.boosters.promise.data.member

import com.boosters.promise.data.member.source.local.LocationSharingPermissionLocalDataSource
import com.boosters.promise.data.member.source.remote.MemberRemoteDataSource
import com.boosters.promise.data.member.source.remote.toMember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRepositoryImpl @Inject constructor(
    private val memberRemoteDataSource: MemberRemoteDataSource,
    private val locationSharingPermissionLocalDataSource: LocationSharingPermissionLocalDataSource
) : MemberRepository {

    override fun initMember(promiseId: String, userCodes: List<String>): Result<Unit> {
        return memberRemoteDataSource.initMembers(promiseId, userCodes)
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

    override fun removeMember(promiseId: String): Result<Unit> {
        return memberRemoteDataSource.removeMember(promiseId)
    }

    override fun removeMember(promiseId: String, userCode: String): Result<Unit> {
        return memberRemoteDataSource.removeMember(promiseId, userCode)
    }

    override suspend fun addIsAcceptLocation(promiseId: String) {
        locationSharingPermissionLocalDataSource.saveLocationSharingPermission(promiseId, false)
    }

    override suspend fun updateIsAcceptLocation(member: Member): Result<Unit> =
        memberRemoteDataSource.updateIsAcceptLocation(member.toMemberBody()).mapCatching {
            locationSharingPermissionLocalDataSource.saveLocationSharingPermission(member.promiseId, member.isAcceptLocation)
        }

    override suspend fun removeIsAcceptLocation(promiseId: String) {
        locationSharingPermissionLocalDataSource.removeLocationSharingPermission(promiseId)
    }

    override fun getIsAcceptLocation(promiseId: String): Flow<Result<Boolean>> =
        locationSharingPermissionLocalDataSource.getLocationSharingPermission(promiseId)

}