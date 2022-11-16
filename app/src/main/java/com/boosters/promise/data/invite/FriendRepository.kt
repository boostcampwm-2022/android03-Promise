package com.boosters.promise.data.invite

import com.boosters.promise.data.invite.source.local.UserEntity

interface FriendRepository {

    suspend fun getFriends(): List<UserEntity>

}