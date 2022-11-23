package com.boosters.promise.ui.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _myInfo = User()
    val myInfo: User get() = _myInfo

    init {
        viewModelScope.launch {
            userRepository.getMyInfo().first().onSuccess {
                _myInfo = it
            }
        }
    }

}