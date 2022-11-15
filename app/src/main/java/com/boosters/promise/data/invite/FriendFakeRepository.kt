package com.boosters.promise.data.invite

import com.boosters.promise.ui.model.UserState

class FriendFakeRepository : FriendRepository {

    override fun getFriends(): List<UserState> {
        return listOf(
            UserState("홍길동", "#0001"),
            UserState("김부캠", "#0002"),
            UserState("아이비", "#0003")
        )
    }

}