package com.boosters.promise.data.user.source.remote

import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {

    suspend fun requestSignUp(userName: String): Result<UserBody>

    fun getUser(userCode: String): Flow<UserBody>

    suspend fun getUserList(userCodeList: List<String>): Flow<List<UserBody>>

    fun getUserByName(userName: String): Flow<List<UserBody>>

}