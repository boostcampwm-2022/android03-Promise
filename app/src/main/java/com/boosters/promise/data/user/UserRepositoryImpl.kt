package com.boosters.promise.data.user

import com.boosters.promise.data.user.source.remote.UserRemoteDataSource
import com.boosters.promise.data.user.source.remote.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun requestSignUp(userName: String): Result<String> =
        userRemoteDataSource.requestSignUp(userName)

    override fun getUser(userCode: String): Flow<User> =
        userRemoteDataSource.getUserBody(userCode).mapNotNull {
            try {
                it.toUser()
            } catch (e: NullPointerException) {
                null
            }
        }

}