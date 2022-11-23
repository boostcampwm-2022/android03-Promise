package com.boosters.promise.ui.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.invite.FriendRepository
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository,
) : ViewModel() {

    private var _myInfo = User()
    val myInfo: User get() = _myInfo

    private val _friendsList = MutableStateFlow(emptyList<User>())
    val friendsList: StateFlow<List<User>> get() = _friendsList.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getMyInfo().first().onSuccess {
                _myInfo = it
            }

            loadFriendsList()
        }
    }

    fun loadFriendsList() {
        viewModelScope.launch {
            _friendsList.value = friendRepository.getFriends()
        }
    }

}