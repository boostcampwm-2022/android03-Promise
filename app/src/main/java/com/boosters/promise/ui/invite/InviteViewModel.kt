package com.boosters.promise.ui.invite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boosters.promise.data.invite.FriendRepository
import com.boosters.promise.ui.model.UserState

class InviteViewModel(
    private val userRepository: FriendRepository
) : ViewModel() {

    private var _friends = MutableLiveData<List<UserState>>()
    val friends: LiveData<List<UserState>> = _friends

    init {
        _friends.value = getFriends()
    }

    private fun getFriends(): List<UserState> {
        return userRepository.getFriends()
    }

}