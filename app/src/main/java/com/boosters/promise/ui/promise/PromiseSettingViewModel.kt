package com.boosters.promise.ui.promise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.User
import com.boosters.promise.ui.promise.model.PromiseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseSettingViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _dialogEventFlow = MutableSharedFlow<EventType>()
    val dialogEventFlow: SharedFlow<EventType> = _dialogEventFlow.asSharedFlow()

    private val _promiseUiState = MutableStateFlow(PromiseUiState())
    val promiseUiState: StateFlow<PromiseUiState> = _promiseUiState.asStateFlow()

    private val _promiseMemberList = MutableLiveData<MutableList<User>?>()
    val promiseMemberList: LiveData<MutableList<User>?> get() = _promiseMemberList

    private fun addMember() {
        val promiseMemberList =
            _promiseMemberList.value ?: emptyList<User>().map { it }.toMutableList()
        //promiseMemberList.add() 선택된 멤버 추가하면 칩이 생긴다.
        _promiseMemberList.value = promiseMemberList
    }

    fun removeMember(removeIndex: Int) {
        val promiseMemberList =
            _promiseMemberList.value?.filterIndexed { index, _ -> index != removeIndex }
                ?.toMutableList()
        _promiseMemberList.value = promiseMemberList
    }

    fun onClickCompletionButton() {
        viewModelScope.launch {
            // promise 객체 넣어주면 firestore에 저장 및 수정됨.
            // promiseRepository.addPromise(promise)
        }
    }

    fun onClickDeleteButton() {
        viewModelScope.launch {
            // promise 객체 넣어주면 firestore에 삭제됨.
            //promiseRepository.removePromise(promise)
        }
    }

    fun onClickPickerEditText(event: EventType) {
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
            it.copy(destinationName = destination)
        }
    }
}