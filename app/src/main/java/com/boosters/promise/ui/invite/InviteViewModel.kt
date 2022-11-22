package com.boosters.promise.ui.invite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.invite.FriendRepository
import com.boosters.promise.data.user.toUserUiState
import com.boosters.promise.ui.invite.model.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {

    private var _currentFriendItems = MutableLiveData<List<UserUiState>?>()
    val currentFriendItems: LiveData<List<UserUiState>?> = _currentFriendItems

    private var _allFriendItems = MutableLiveData<List<UserUiState>>()
    private val allFriendItems: LiveData<List<UserUiState>> = _allFriendItems

    private var _currentMemberItems = MutableLiveData<List<UserUiState>?>()
    val currentMemberItems: LiveData<List<UserUiState>?> = _currentMemberItems

    fun initAllFriendItems() {
        viewModelScope.launch {
            val data = friendRepository.getFriends()
            _allFriendItems.value = data.map { user ->
                user.toUserUiState()
            }
            val inviteCheckedItems = allFriendItems.value?.map { user ->
                if (currentMemberItems.value?.contains(user) == true) {
                    user.isSelected = true
                }
                user
            }
            _currentFriendItems.value = inviteCheckedItems
        }
    }

    fun initMemberItems(memberItems: List<UserUiState>?) {
        _currentMemberItems.value = memberItems
    }

    fun searchFriendItems(query: String) {
        _currentFriendItems.value = allFriendItems.value?.filter { user ->
            if (query.matches(userCodeRegex)) {
                user.userCode.contains(query)
            } else {
                user.userName.contains(query)
            }
        }
    }

    companion object {
        private val userCodeRegex = Regex("""#\d+""")
    }

}