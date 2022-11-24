package com.boosters.promise.data.user

import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.user.source.local.MyInfoLocalDataSource
import com.boosters.promise.data.user.source.remote.UserRemoteDataSource
import com.boosters.promise.data.user.source.remote.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val myInfoLocalDataSource: MyInfoLocalDataSource
) : UserRepository {

    override suspend fun requestSignUp(userName: String): Result<User> {
        return userRemoteDataSource.requestSignUp(userName).mapCatching { userBody ->
            val user = userBody.toUser()
            myInfoLocalDataSource.saveMyInfo(user)

            user
        }
    }

    override fun getUser(userCode: String): Flow<User> =
        userRemoteDataSource.getUser(userCode).mapNotNull { userBody ->
            try {
                userBody.toUser()
            } catch (e: NullPointerException) {
                null
            }
        }

    override fun getUserByName(userName: String): Flow<List<User>> =
        userRemoteDataSource.getUserByName(userName).mapNotNull { userList ->
            try {
                userList.map { it.toUser() }
            } catch (e: NullPointerException) {
                null
            }
        }

    override fun getMyInfo(): Flow<Result<User>> =
        myInfoLocalDataSource.getMyInfo()

    override suspend fun uploadMyGeoLocation(geoLocation: GeoLocation?): Result<Unit> = runCatching {
        val user = getMyInfo().first().getOrThrow()
        userRemoteDataSource.uploadMyGeoLocation(user.userCode, geoLocation)
    }

    override suspend fun resetMyGeoLocation() {
        val user = getMyInfo().first().getOrNull()
        if (user != null) {
            userRemoteDataSource.resetMyGeoLocation(user.userCode)
        }
    }

    override suspend fun getUserList(userCodeList: List<String>): List<User> {
        return userRemoteDataSource.getUserList(userCodeList).map { it.toUser() }
    }

}