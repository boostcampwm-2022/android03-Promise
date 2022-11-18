package com.boosters.promise.ui.promise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.ui.invite.model.UserUiState
import com.boosters.promise.ui.promise.model.PromiseSettingUiState
import com.boosters.promise.ui.promise.model.PromiseUiState
import com.boosters.promise.ui.promise.model.toPromise
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

    private val _promiseSettingUiState: MutableStateFlow<PromiseSettingUiState> =
        MutableStateFlow(PromiseSettingUiState.Empty(PromiseUiState()))
    val promiseSettingUiState: StateFlow<PromiseSettingUiState> =
        _promiseSettingUiState.asStateFlow()

    fun updateMember(newMemberList: List<UserUiState>) {
        _promiseSettingUiState.update {
            PromiseSettingUiState.Empty(it.promise.copy(members = newMemberList))
        }
    }

    fun removeMember(removeMember: UserUiState) {
        _promiseSettingUiState.update {
            val memberList =
                it.promise.members.filter { member -> member.userCode != removeMember.userCode }
            PromiseSettingUiState.Empty(it.promise.copy(members = memberList))
        }
    }

    fun onClickCompletionButton() {
        viewModelScope.launch {
            val promise =
                _promiseSettingUiState.value.promise.copy(promiseId = "z59PQAn8w4CimSw0kdB1")
                    .toPromise()
            promiseRepository.addPromise(promise).collect {
                when (it) {
                    true -> _promiseSettingUiState.emit(
                        PromiseSettingUiState.Success
                    )
                    false -> _promiseSettingUiState.emit(
                        PromiseSettingUiState.Fail(
                            _promiseSettingUiState.value.promise
                        )
                    )
                }
            }
        }
    }

    fun onClickPickerEditText(event: EventType) {
        viewModelScope.launch {
            _dialogEventFlow.emit(event)
        }
    }

    fun setPromiseDate(date: String) {
        _promiseSettingUiState.update {
            PromiseSettingUiState.Empty(it.promise.copy(date = date))
        }
    }

    fun setPromiseTime(time: String) {
        _promiseSettingUiState.update {
            PromiseSettingUiState.Empty(it.promise.copy(time = time))
        }
    }

    fun setPromiseDestination(destination: String) {
        _promiseSettingUiState.update {
            PromiseSettingUiState.Empty(it.promise.copy(destinationName = destination))
        }
    }

    fun setPromiseTitle(title: String) {
        _promiseSettingUiState.update {
            PromiseSettingUiState.Empty(it.promise.copy(title = title))
        }
    }

    fun initPromiseSettingUiState() {
        _promiseSettingUiState.update {
            PromiseSettingUiState.Empty(it.promise)
        }
    }

}