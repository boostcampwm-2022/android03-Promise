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
    val allFriendItems: LiveData<List<UserUiState>> = _allFriendItems

    fun loadAllFriendItems() {
        viewModelScope.launch {
            val data = friendRepository.getFriends()
            _allFriendItems.value = data.map { user ->
                user.toUserUiState()
            }
        }
    }

    fun setCurrentFriendItems(items: List<UserUiState>?) {
        _currentFriendItems.value = items
    }

    fun searchFriendItems(query: String) {
        setCurrentFriendItems(allFriendItems.value?.filter { user ->
            if (query.matches(userCodeRegex)) {
                user.userCode.contains(query)
            } else {
                user.userName.contains(query)
            }
        })
    }

    companion object {
        private val userCodeRegex = Regex("""#\d+""")
    }

}