package com.boosters.promise.data.promise

import com.boosters.promise.data.member.MemberRepository
import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSource
import com.boosters.promise.data.promise.source.remote.toPromise
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.UserRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PromiseRepositoryImpl @Inject constructor(
    private val promiseRemoteDataSource: PromiseRemoteDataSource,
    private val userRepository: UserRepository,
    private val memberRepository: MemberRepository
) : PromiseRepository {

    override suspend fun addPromise(promise: Promise): Flow<Result<String>> {
        return promiseRemoteDataSource.addPromise(promise.toPromiseBody()).map { result ->
            result.mapCatching { promiseId ->
                memberRepository.initMember(promiseId, promise.members.map { member -> member.userCode })
                memberRepository.addIsAcceptLocation(promiseId)
                promiseId
            }
        }
    }

    override suspend fun modifyPromise(promise: Promise): Flow<Result<String>> {
        val promiseBody = promise.toPromiseBody()
        return promiseRemoteDataSource.addPromise(promiseBody).map { result ->
            result.mapCatching { promiseId ->
                val memberCodes = memberRepository.getMembers(promiseId).first().map { it.userCode }
                promiseBody.members.filterNot { it in memberCodes }.let {
                    memberRepository.initMember(promiseId, it)
                }
                memberCodes.filterNot { it in promiseBody.members }.forEach {
                    memberRepository.removeMember(promiseId, it)
                }
                promiseId
            }
        }
    }

    override fun removePromise(promiseId: String): Flow<Boolean> {
        return promiseRemoteDataSource.removePromise(promiseId).onEach { isRemoveSuccess ->
            if (isRemoveSuccess) memberRepository.removeMember(promiseId)
        }
    }

    override fun getPromise(promiseId: String): Flow<Promise?> {
        return promiseRemoteDataSource.getPromise(promiseId).map { promiseBody ->
            promiseBody?.toPromise(
                promiseBody.members.map {
                    userRepository.getUser(it).first()
                }
            )
        }
    }

    override fun getPromiseList(user: User): Flow<List<Promise>> {
        return promiseRemoteDataSource.getPromiseList(user).map { promiseList ->
            promiseList.mapNotNull { promiseBody ->
                try {
                    promiseBody.toPromise(userRepository.getUserList(promiseBody.members).first())
                } catch (e: NullPointerException) {
                    null
                }
            }
        }
    }

}