package com.boosters.promise.data.user

import com.boosters.promise.data.user.source.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun requestSignUp(userName: String): Result<User> =
        userRemoteDataSource.requestSignUp(userName)

    override fun getUser(userCode: String): Flow<User> =
        userRemoteDataSource.getUser(userCode)

}