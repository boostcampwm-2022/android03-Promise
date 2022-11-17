package com.boosters.promise.data.user

import com.boosters.promise.data.user.source.local.MyInfoLocalDataSource
import com.boosters.promise.data.user.source.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val myInfoLocalDataSource: MyInfoLocalDataSource
) : UserRepository {

    override suspend fun requestSignUp(userName: String): Result<User> {
        return userRemoteDataSource.requestSignUp(userName).also {
            myInfoLocalDataSource.saveMyInfo(it.getOrThrow())
        }
    }

    override fun getUser(userCode: String): Flow<User> =
        userRemoteDataSource.getUser(userCode)

    override suspend fun getMyInfo(): Flow<Result<User>> =
        myInfoLocalDataSource.getMyInfo()

}