package com.boosters.promise.data.invite

import com.boosters.promise.data.invite.source.local.FriendLocalDataSource
import com.boosters.promise.data.invite.source.local.toUser
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.toUserEntity
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendLocalDataSource: FriendLocalDataSource
) : FriendRepository {

    override suspend fun getFriends(): List<User> {
        return friendLocalDataSource.getFriends().map { userEntity ->
            userEntity.toUser()
        }
    }

    override suspend fun addFriend(user: User) {
        friendLocalDataSource.insertFriend(user.toUserEntity())
    }

}