package com.boosters.promise.ui.promisecalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseCalendarViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _promiseDailyList = MutableStateFlow<List<Promise>>(emptyList())
    val promiseDailyList: StateFlow<List<Promise>> get() = _promiseDailyList.asStateFlow()

    fun updatePromiseList(date: String) {
        viewModelScope.launch {
            userRepository.getMyInfo().first().onSuccess { myInfo ->
                promiseRepository.getPromiseList(myInfo, date).collectLatest {
                    _promiseDailyList.value = it
                }
            }
        }
    }

}