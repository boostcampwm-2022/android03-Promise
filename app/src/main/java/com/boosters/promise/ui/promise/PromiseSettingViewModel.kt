package com.boosters.promise.ui.promise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.model.Location
import com.boosters.promise.R
import com.boosters.promise.data.notification.NotificationRepository
import com.boosters.promise.data.notification.source.remote.NotificationRequestBody
import com.boosters.promise.data.notification.source.remote.NotificationRequestData
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.data.user.toUserUiState
import com.boosters.promise.ui.invite.model.UserUiState
import com.boosters.promise.ui.promise.model.PromiseSettingEvent
import com.boosters.promise.ui.promise.model.PromiseSettingUiState
import com.boosters.promise.ui.promise.model.PromiseUiState
import com.boosters.promise.ui.promise.model.toPromise
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PromiseSettingViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _dialogEventFlow = MutableSharedFlow<PromiseSettingEvent>()
    val dialogEventFlow: SharedFlow<PromiseSettingEvent> = _dialogEventFlow.asSharedFlow()

    private val _promiseUiState = MutableStateFlow(PromiseUiState())
    val promiseUiState: StateFlow<PromiseUiState> = _promiseUiState.asStateFlow()

    private val _promiseSettingUiState = MutableSharedFlow<PromiseSettingUiState>()
    val promiseSettingUiState: SharedFlow<PromiseSettingUiState> = _promiseSettingUiState.asSharedFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    private lateinit var myInfo: User

    init {
        viewModelScope.launch {
            userRepository.getMyInfo().first().onSuccess {
                myInfo = it
            }
        }
        sendNotification()
    }

    fun updateMember(newMemberList: List<UserUiState>) {
        _promiseUiState.update {
            it.copy(members = newMemberList)
        }
    }

    fun removeMember(removeMember: UserUiState) {
        _promiseUiState.update {
            it.copy(members = it.members.filter { member -> member.userCode != removeMember.userCode })
        }
    }

    fun onClickCompletionButton() {
        val promise = _promiseUiState.value
        if (promise.title.isEmpty() || promise.time.isEmpty() || promise.destinationName.isEmpty() || promise.date.isEmpty()) {
            changeUiState(PromiseSettingUiState.Fail(R.string.promiseSetting_empty))
            return
        }
        val promiseTime = LocalDateTime.parse("${promise.date} ${promise.time}", dateFormatter)
        if (promiseTime.isBefore(LocalDateTime.now())) {
            changeUiState(PromiseSettingUiState.Fail(R.string.promiseSetting_beforeTime))
            return
        }
        viewModelScope.launch {
            val members = promise.members.toMutableList()
            members.add(myInfo.toUserUiState())
            promiseRepository.addPromise(promise.copy(members = members).toPromise()).collect {
                when (it) {
                    true -> changeUiState(PromiseSettingUiState.Success)
                    false -> changeUiState(PromiseSettingUiState.Fail(R.string.promiseSetting_fail))
                }
            }
        }
    }

    fun onClickPickerEditText(event: PromiseSettingEvent) {
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

    fun setPromiseDestination(destinationName: String, destinationLocation: Location) {
        _promiseUiState.update {
            it.copy(destinationName = destinationName, destinationLocation = destinationLocation)
        }
    }

    fun setPromiseTitle(title: String) {
        _promiseUiState.update {
            it.copy(title = title)
        }
    }

    private fun changeUiState(state: PromiseSettingUiState) {
        viewModelScope.launch {
            _promiseSettingUiState.emit(state)
        }
    }

    fun cloudMessaging() {
        viewModelScope.launch {
            FirebaseMessaging.getInstance().token.await()
        }
    }

    private fun sendNotification() {
        val notificationRequestBody =
            NotificationRequestBody("coJP1ozcQ26vxnBOE6t2vX:APA91bH_TMyoECawIWHJ3tctiT1FQP3SHKnIB_qYh5lJ7MQdMDaJwXT0P49OQrrLpmuBtDqP1a3Mw_FXwx6iA1fbSIRxFg8STPwJaInUU_MpwahDWmPJjREhKkR9hzINo2zfynMHaFoO"
                , NotificationRequestData("Promise", "hello"))
        viewModelScope.launch {
            notificationRepository.sendNotification(notificationRequestBody)
        }
    }

}