package com.boosters.promise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PromiseSettingViewModel : ViewModel() {

    private val _promiseMemberList = MutableLiveData<MutableList<User>?>()
    val promiseMemberList: LiveData<MutableList<User>?> get() = _promiseMemberList
    var count = 0

    private fun addMember() {
        val promiseMemberList = _promiseMemberList.value ?: emptyList<User>().map { it }.toMutableList()
        promiseMemberList.add(User("123","유수미${count++}",Location(1.0,2.0)))
        _promiseMemberList.value = promiseMemberList
    }

    fun removeMember(removeIndex: Int) {
        val promiseMemberList = _promiseMemberList.value?.filterIndexed { index, _ -> index != removeIndex }?.toMutableList()
        _promiseMemberList.value = promiseMemberList
    }

    fun onClickCompletionButton() {
        addMember()
    }

}