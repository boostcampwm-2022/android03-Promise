package com.boosters.promise.data.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun requestSignUp(userName: String): Result<User>

    fun getAllUsers(): Flow<List<User>>

    fun getUser(userCode: String): Flow<User>

    fun getMyInfo(): Flow<Result<User>>

}