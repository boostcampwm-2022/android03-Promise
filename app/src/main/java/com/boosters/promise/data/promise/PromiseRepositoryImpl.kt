package com.boosters.promise.data.promise

import com.boosters.promise.data.member.MemberRepository
import com.boosters.promise.data.promise.source.remote.PromiseRemoteDataSource
import com.boosters.promise.data.promise.source.remote.toPromise
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
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

    override fun removePromise(promiseId: String): Flow<Boolean> {
        return promiseRemoteDataSource.removePromise(promiseId)
    }

    override fun getPromise(promiseId: String): Flow<Promise> {
        return promiseRemoteDataSource.getPromise(promiseId).mapNotNull { promiseBody ->
            promiseBody.toPromise(userRepository.getUserList(promiseBody.members).first())
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