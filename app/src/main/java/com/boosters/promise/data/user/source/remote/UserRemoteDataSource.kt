package com.boosters.promise.data.user.source.remote

import com.boosters.promise.data.user.User
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {

    suspend fun requestSignUp(userName: String): Result<User>

    fun getUser(userCode: String): Flow<User>

}