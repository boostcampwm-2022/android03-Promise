package com.boosters.promise.data.friend

import com.boosters.promise.data.user.User

interface FriendRepository {

    suspend fun getFriends(): List<User>

    suspend fun addFriend(user: User)

}