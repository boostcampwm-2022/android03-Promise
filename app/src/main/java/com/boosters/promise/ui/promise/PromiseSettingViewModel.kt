package com.boosters.promise.ui.promise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseSettingViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _dialogEventFlow = MutableSharedFlow<Event>()
    val dialogEventFlow: SharedFlow<Event> = _dialogEventFlow.asSharedFlow()

    private val _promiseUiState = MutableStateFlow(PromiseUiState())
    val promiseUiState: StateFlow<PromiseUiState> = _promiseUiState.asStateFlow()

    private val _promiseMemberList = MutableLiveData<MutableList<User>?>()
    val promiseMemberList: LiveData<MutableList<User>?> get() = _promiseMemberList
    private var count = 0

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
        val promise = Promise(
            "",
            "꽁치와의 데이트",
            "홍대",
            1,
            2,
            "2022/11/14",
            "12:00",
            listOf(user)
        )
        viewModelScope.launch {
            promiseRepository.addPromise(promise)
        }
        addMember()
    }

    fun onClickUpdateButton() {
        val user = User("123", "유수미${count}", 1.0, 2.0)
        val promise = Promise(
            "-NGu0GJFZKmY4qHTirxB",
            "엄마와의 데이트",
            "영등포",
            1,
            2,
            "2022/11/13",
            "12:00",
            listOf(user)
        )
        viewModelScope.launch {
            promiseRepository.addPromise(promise)
            val promiseList = promiseRepository.getPromiseList("2022/11/14")
        }
    }

    fun onClickDeleteButton() {
        val user = User("123", "유수미${count}", 1.0, 2.0)
        val promise = Promise(
            "-NGu05_dzkrSv1IF75pM",
            "엄마와의 데이트",
            "영등포",
            1,
            2,
            "2022/11/13",
            "12:00",
            listOf(user)
        )
        viewModelScope.launch {
            promiseRepository.removePromise(promise)
        }
    }

    fun onClickPickerEditText(event: Event) {
        viewModelScope.launch {
            _dialogEventFlow.emit(event)
        }
    }

    fun setPromiseDate(date: String) {
        _promiseUiState.update {
            it.copy(date = date)
        }
    }

    fun setPromiseTime(time: String) {
        _promiseUiState.update {
            it.copy(time = time)
        }
    }

    fun setPromiseDestination(destination: String) {
        _promiseUiState.update {
            it.copy(destination = destination)
        }
    }
}