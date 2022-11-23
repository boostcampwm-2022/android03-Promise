package com.boosters.promise.ui.promise

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.model.Location
import com.boosters.promise.R
import com.boosters.promise.data.notification.NotificationRepository
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.promise.ServerKeyRepository
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
    private val serverKeyRepository: ServerKeyRepository,
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
                    true -> sendNotification()
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
        val members = listOf(
            User(
            "8WC1UT",
            "name2",
            userToken = "fL1BdTxZQY6jk0C-JuvjZT:APA91bEVbE7avFbgSYhSL5HsEe3n84BWZM9LRXSBAmtiAkurGqLs2LMdX4Q_f25MQ3PqeBFgkJcn9KRKnDYsFKqr_Sw3q-VwFHjTv-Ec8ippW0_kihHRw7-GQSARC6dNfVOIm1cX_ufT"
        ),
            User(
                "uGilLB",
                "name",
                userToken = "drlAv3AsSCCKx7DVionZ6t:APA91bGIACuNXBdWXvbRVIjXJTJ8lOBUQnj1iU_OSm5I7q2GcRkOJLzwcK7Yyli0fRKzT9NoXIkjNecsSfFIxV8vq2vxH_MBezOBfVpZaJ4prbtN1N3qETgN0ztHx5jvzNJ-bRHE1_Yj"),
            User(
                "zOH89U",
                "name",
                userToken = "fbu_E84FSJqgBG0c0N2UKf:APA91bEm_kDXB1QkGpIJwPUbLjy--WQajclBzoczVMfSGUFQrL0Zt0Ec4gutel56rM2JefRV4A9"))
        Log.d("MainActivity", "$myInfo")
        viewModelScope.launch {
            val userCodeList = members.filter { it.userCode != myInfo.userCode }.map { it.userCode }
            if (userCodeList.isEmpty()) return@launch
            val key = serverKeyRepository.getServerKey()
            userRepository.getUserList(userCodeList).forEach { user ->
                notificationRepository.sendNotification(_promiseUiState.value.title, "[${_promiseUiState.value.date}] 새로운 약속이 추가 되었습니다.", user.userToken, key)
            }
            changeUiState(PromiseSettingUiState.Success)
        }
    }

}