package com.boosters.promise.ui.invite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.invite.FriendRepository
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.invite.model.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {

    private var _friends = MutableLiveData<List<UserUiState>>()
    val friends: LiveData<List<UserUiState>> = _friends

    init {
        getFriends()
    }

    private fun getFriends() {
        viewModelScope.launch {
            val data = friendRepository.getFriends()
            _friends.value = data.map { user ->
                UserUiState(
                    userName = user.userName,
                    userCode = user.userCode
                )
            }
        }
    }

}