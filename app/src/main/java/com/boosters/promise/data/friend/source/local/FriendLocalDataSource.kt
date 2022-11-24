package com.boosters.promise.data.friend.source.local

interface FriendLocalDataSource {

    suspend fun getFriends(): List<FriendEntity>

    suspend fun insertFriend(user: FriendEntity)

}