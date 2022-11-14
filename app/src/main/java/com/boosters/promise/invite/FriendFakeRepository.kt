package com.boosters.promise.invite

class FriendFakeRepository : FriendRepository {
    override fun getFriends(): List<User> {
        return listOf(
            User("홍길동", "#0001"),
            User("김부캠", "#0002"),
            User("아이비", "#0003")
        )
    }
}