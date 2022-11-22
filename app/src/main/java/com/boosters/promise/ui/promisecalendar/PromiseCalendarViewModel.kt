package com.boosters.promise.ui.promisecalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.promise.toPromiseUiState
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.promisesetting.model.PromiseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseCalendarViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _promiseDailyList = MutableStateFlow(listOf(PromiseUiState()))
    val promiseDailyList: StateFlow<List<PromiseUiState>> get() = _promiseDailyList.asStateFlow()

    fun getPromiseList(date: String) {
        viewModelScope.launch {
            userRepository.getMyInfo().first().onSuccess { myInfo ->
                val resultPromiseList = promiseRepository.getPromiseList(myInfo, date).map { promise ->
                    promise.toPromiseUiState()
                }
                _promiseDailyList.update { resultPromiseList }
            }
        }
    }
}