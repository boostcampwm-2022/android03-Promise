package com.boosters.promise.data.invite

import com.boosters.promise.data.user.User

interface FriendRepository {

    suspend fun getFriends(): List<User>

}