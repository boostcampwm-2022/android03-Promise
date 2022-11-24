package com.boosters.promise.ui.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.friend.FriendRepository
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository
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

    fun addFriend(user: User) {
        viewModelScope.launch {
            friendRepository.addFriend(user)
            _usersList.update { beforeUserList ->
                beforeUserList.filterNot { it.userCode == user.userCode }
            }
        }
    }

    fun searchFriend(query: String) {
        viewModelScope.launch {
            val allFriendList = _usersList.value

            _usersList.value = if (query.matches(USER_CODE_REGEX)) allFriendList.filter { user ->
                user.userCode.contains(query.removePrefix("#"))
            } else allFriendList.filter { user ->
                user.userName.contains(query)
            }
        }
    }

    fun searchUser(query: String) {
        viewModelScope.launch {
            val filterUserCodeList = friendRepository.getFriends().map {
                it.userCode
            } + myInfo.userCode

            val result = if (query.matches(USER_CODE_REGEX)) {
                listOf(userRepository.getUser(query.removePrefix("#")).first())
            } else userRepository.getUserByName(query).first()

            _usersList.value = result.filterNot { it.userCode in filterUserCodeList }
        }
    }

    companion object {
        private val USER_CODE_REGEX = Regex("#[a-zA-Z0-9]{1,6}")
    }

}