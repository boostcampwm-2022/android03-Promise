package com.boosters.promise.data.user.source.local

import com.boosters.promise.data.user.User
import kotlinx.coroutines.flow.Flow

interface MyInfoLocalDataSource {

    suspend fun saveMyInfo(user: User)

    fun getMyInfo(): Flow<Result<User>>

}