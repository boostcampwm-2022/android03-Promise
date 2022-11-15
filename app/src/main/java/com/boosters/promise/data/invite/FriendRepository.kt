package com.boosters.promise.data.invite

import com.boosters.promise.data.invite.source.local.User

interface FriendRepository {

    suspend fun getFriends(): List<User>

}