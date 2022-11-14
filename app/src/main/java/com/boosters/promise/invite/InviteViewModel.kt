package com.boosters.promise.invite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InviteViewModel(
    private val userRepository: FriendRepository
) : ViewModel() {
    private var _friends = MutableLiveData<List<User>>()
    val friends: LiveData<List<User>> = _friends

    init {
        _friends.value = getFriends()
    }

    private fun getFriends(): List<User> {
        return userRepository.getFriends()
    }
}