package com.boosters.promise.data.friend.source.local

import javax.inject.Inject

class FriendLocalDataSourceImpl @Inject constructor(
    private val friendDao: FriendDao
) : FriendLocalDataSource {

    override suspend fun getFriends(): List<FriendEntity> {
        return friendDao.getFriends()
    }

    override suspend fun insertFriend(user: FriendEntity) {
        friendDao.insertFriend(user)
    }

}