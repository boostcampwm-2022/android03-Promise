package com.boosters.promise.data.user

import com.boosters.promise.data.user.source.local.MyInfoLocalDataSource
import com.boosters.promise.data.user.source.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val myInfoLocalDataSource: MyInfoLocalDataSource
) : UserRepository {

    override suspend fun requestSignUp(userName: String): Result<User> {
        return userRemoteDataSource.requestSignUp(userName).onSuccess {
            myInfoLocalDataSource.saveMyInfo(it)
        }
    }

    override fun getUser(userCode: String): Flow<User> =
        userRemoteDataSource.getUser(userCode)

    override fun getMyInfo(): Flow<Result<User>> =
        myInfoLocalDataSource.getMyInfo()

    override suspend fun getUserList(userCodeList: List<String>): List<User> {
        return userRemoteDataSource.getUserList(userCodeList)
    }

}