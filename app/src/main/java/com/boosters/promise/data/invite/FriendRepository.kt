package com.boosters.promise.data.invite

import com.boosters.promise.ui.model.UserState

interface FriendRepository {

    fun getFriends(): List<UserState>

}