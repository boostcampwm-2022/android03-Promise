package com.boosters.promise

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseSettingViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _promiseMemberList = MutableLiveData<MutableList<User>?>()
    private val database = Firebase.database("https://promise-c3e7d-default-rtdb.firebaseio.com/")
    private val promiseRef = database.getReference("promise")
    val promiseMemberList: LiveData<MutableList<User>?> get() = _promiseMemberList
    var count = 0

    init {
        promiseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value as HashMap<String, Promise>
                value.keys.forEach { key ->
                    Log.d("MainActivity", "${value[key]}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("MainActivity", "없음")
            }
        })
    }

    private fun addMember() {
        val promiseMemberList =
            _promiseMemberList.value ?: emptyList<User>().map { it }.toMutableList()
        promiseMemberList.add(User("123", "유수미${count++}", 1.0, 2.0))
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
        val promise =
            Promise("$count", "꽁치와의 데이트", "홍대", 1.0, 2.0, "2022/11/14", "12:00", listOf(user))
        viewModelScope.launch {
            promiseRepository.addPromise(promise)
        }
        //promiseRef.push().setValue(promise)
        addMember()
    }

}