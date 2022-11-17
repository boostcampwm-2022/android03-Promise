package com.boosters.promise.data.invite.source.local

interface FriendLocalDataSource {

    suspend fun getFriends(): List<UserEntity>

}