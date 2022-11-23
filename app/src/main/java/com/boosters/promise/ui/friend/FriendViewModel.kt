package com.boosters.promise.ui.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.invite.FriendRepository
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository,
) : ViewModel() {

    private var _myInfo = User()
    val myInfo: User get() = _myInfo

    private val _usersList = MutableStateFlow(emptyList<User>())
    val usersList: StateFlow<List<User>> get() = _usersList.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getMyInfo().first().onSuccess {
                _myInfo = it
            }
        }
        loadFriendsList()
    }

    fun loadFriendsList() {
        viewModelScope.launch {
            _usersList.value = friendRepository.getFriends()
        }
    }

    fun loadAllUsersList() {
        viewModelScope.launch {
            userRepository.getAllUsers().collectLatest { userList ->
                _usersList.value = userList
            }
        }
    }

}