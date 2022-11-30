package com.boosters.promise.data.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun requestSignUp(userName: String): Result<User>

    fun getUser(userCode: String): Flow<User>

    fun getUserByName(userName: String): Flow<List<User>>

    fun getMyInfo(): Flow<Result<User>>

    suspend fun getUserList(userCodeList: List<String>): Flow<List<User>>

}