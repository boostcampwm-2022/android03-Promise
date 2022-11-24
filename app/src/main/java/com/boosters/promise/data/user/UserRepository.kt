package com.boosters.promise.data.user

import com.boosters.promise.data.location.GeoLocation
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun requestSignUp(userName: String): Result<User>

    fun getUser(userCode: String): Flow<User>

    fun getUserByName(userName: String): Flow<List<User>>

    fun getMyInfo(): Flow<Result<User>>

    suspend fun uploadMyGeoLocation(geoLocation: GeoLocation?): Result<Unit>

    suspend fun resetMyGeoLocation()

    suspend fun getUserList(userCodeList: List<String>): Flow<List<User>>

}