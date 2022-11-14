package com.boosters.promise.invite

interface FriendRepository {
    fun getFriends(): List<User>
}