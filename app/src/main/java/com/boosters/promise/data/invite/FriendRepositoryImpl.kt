package com.boosters.promise.data.invite

import com.boosters.promise.data.invite.source.local.FriendLocalDataSource
import com.boosters.promise.data.invite.source.local.UserEntity
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendLocalDataSource: FriendLocalDataSource
) : FriendRepository {

    override suspend fun getFriends(): List<UserEntity> {
        return friendLocalDataSource.getFriends()
    }

}