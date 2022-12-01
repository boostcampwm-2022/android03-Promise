package com.boosters.promise.ui.invite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.friend.FriendRepository
import com.boosters.promise.data.user.toUserUiModel
import com.boosters.promise.ui.invite.model.UserUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {

    private var _currentFriendItems = MutableLiveData<List<UserUiModel>?>()
    val currentFriendItems: LiveData<List<UserUiModel>?> = _currentFriendItems

    private var allFriendItems: List<UserUiModel>? = null

    private var _currentMemberItems = MutableLiveData<List<UserUiModel>?>()
    val currentMemberItems: LiveData<List<UserUiModel>?> = _currentMemberItems

    fun initAllFriendItems() {
        viewModelScope.launch {
            val data = friendRepository.getFriends()
            allFriendItems = data.map { user ->
                user.toUserUiModel()
            }
            val inviteCheckedItems = allFriendItems?.map { user ->
                currentMemberItems.value?.find { it.userCode == user.userCode }?.copy(isSelected = true) ?: user
            }
            _currentFriendItems.value = inviteCheckedItems
        }
    }

    fun initMemberItems(memberItems: List<UserUiModel>?) {
        _currentMemberItems.value = memberItems
    }

    fun addMemberItems(user: UserUiModel) {
        if (user.isSelected.not()) {
            _currentMemberItems.value = currentMemberItems.value?.plusElement(user)
            _currentFriendItems.value = currentFriendItems.value?.map {
                if (it.userCode == user.userCode) it.copy(isSelected = true) else it
            }
        }
    }

    fun removeMemberItems(user: UserUiModel) {
        _currentMemberItems.value = _currentMemberItems.value?.minusElement(user)
        _currentFriendItems.value = currentFriendItems.value?.map {
            if (it.userCode == user.userCode) it.copy(isSelected = false) else it
        }
    }

    fun searchFriendItems(query: String) {
        _currentFriendItems.value = allFriendItems?.filter { user ->
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