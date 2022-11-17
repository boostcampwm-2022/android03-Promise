package com.boosters.promise.data.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun requestSignUp(userName: String): Result<User>

    fun getUser(userCode: String): Flow<User>

    suspend fun getMyInfo(): Flow<Result<User>>

}