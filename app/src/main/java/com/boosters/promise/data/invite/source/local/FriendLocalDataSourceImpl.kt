package com.boosters.promise.data.invite.source.local

import com.boosters.promise.data.database.UserDataBase
import javax.inject.Inject

class FriendLocalDataSourceImpl @Inject constructor(
    private val userDataBase: UserDataBase
): FriendLocalDataSource {

    override suspend fun getFriends(): List<User> {
        return userDataBase.userDao().getFriends()
    }

}