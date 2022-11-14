package com.boosters.promise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseSettingViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _promiseMemberList = MutableLiveData<MutableList<User>?>()
    val promiseMemberList: LiveData<MutableList<User>?> get() = _promiseMemberList
    var count = 0

    private fun addMember() {
        val promiseMemberList =
            _promiseMemberList.value ?: emptyList<User>().map { it }.toMutableList()
        promiseMemberList.add(User("123", "유수미${count}", 1.0, 2.0))
        _promiseMemberList.value = promiseMemberList
    }

    fun removeMember(removeIndex: Int) {
        val promiseMemberList =
            _promiseMemberList.value?.filterIndexed { index, _ -> index != removeIndex }
                ?.toMutableList()
        _promiseMemberList.value = promiseMemberList
    }

    fun onClickCompletionButton() {
        val user = User("123", "유수미${count++}", 1.0, 2.0)
        val promise = Promise("$count", "꽁치와의 데이트", "홍대", 1.0, 2.0, "2022/11/14", "12:00", listOf(user))
        viewModelScope.launch {
            promiseRepository.addPromise(promise)
        }
        addMember()
    }

    fun onClickUpdateButton() {
        val user = User("123", "유수미${count}", 1.0, 2.0)
        val promise = Promise("1", "엄마와의 데이트", "영등포", 1.0, 2.0, "2022/11/14", "12:00", listOf(user))
        viewModelScope.launch {
            promiseRepository.updatePromise(promise)
        }
    }

    fun onClickDeleteButton() {
        val user = User("123", "유수미${count}", 1.0, 2.0)
        val promise = Promise("1", "엄마와의 데이트", "영등포", 1.0, 2.0, "2022/11/14", "12:00", listOf(user))
        viewModelScope.launch {
            promiseRepository.removePromise(promise)
        }
    }

}