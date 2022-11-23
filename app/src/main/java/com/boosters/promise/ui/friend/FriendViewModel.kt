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

    private var allUserList: List<User> = emptyList()

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
            allUserList = _usersList.value
        }
    }

    fun loadAllUsersList() {
        viewModelScope.launch {
            userRepository.getAllUsers().collectLatest { userList ->
                val friendsCodeList = friendRepository.getFriends().map { friend ->
                    friend.userCode
                } + myInfo.userCode

                _usersList.value = userList.filterNot { stranger ->
                    stranger.userCode in friendsCodeList
                }

                allUserList = _usersList.value
            }
        }
    }

    fun addFriend(user: User) {
        viewModelScope.launch {
            friendRepository.addFriend(user)
        }
    }

    fun searchUser(query: String) {
        viewModelScope.launch {
            _usersList.value = if (query.matches(USER_CODE_REGEX)) allUserList.filter { user ->
                user.userCode.contains(query.removePrefix("#"))
            } else allUserList.filter { user ->
                user.userName.contains(query)
            }
        }
    }

    companion object {
        private val USER_CODE_REGEX = Regex("#[a-zA-Z0-9]{1,6}")
    }

}