package com.boosters.promise.data.invite.source.local

import javax.inject.Inject

class FriendLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : FriendLocalDataSource {

    override suspend fun getFriends(): List<UserEntity> {
        return userDao.getFriends()
    }

}