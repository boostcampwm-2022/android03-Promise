package com.boosters.promise.data.friend

import com.boosters.promise.data.friend.source.local.FriendLocalDataSource
import com.boosters.promise.data.friend.source.local.toUser
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.toFriendEntity
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendLocalDataSource: FriendLocalDataSource
) : FriendRepository {

    override suspend fun getFriends(): List<User> {
        return friendLocalDataSource.getFriends().map { friendEntity ->
            friendEntity.toUser()
        }
    }

    override suspend fun addFriend(user: User) {
        friendLocalDataSource.insertFriend(user.toFriendEntity())
    }

}