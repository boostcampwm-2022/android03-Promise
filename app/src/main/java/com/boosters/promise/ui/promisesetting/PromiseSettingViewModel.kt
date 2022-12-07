package com.boosters.promise.ui.promisesetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.R
import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.network.NetworkConnectionUtil
import com.boosters.promise.data.notification.NotificationRepository
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.invite.model.UserUiModel
import com.boosters.promise.ui.invite.model.toUser
import com.boosters.promise.ui.notification.AlarmDirector
import com.boosters.promise.ui.notification.NotificationService
import com.boosters.promise.ui.promisesetting.model.PromiseSettingEvent
import com.boosters.promise.ui.promisesetting.model.PromiseSettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PromiseSettingViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository,
    private val alarmDirector: AlarmDirector,
    private val networkConnectionUtil: NetworkConnectionUtil
) : ViewModel() {

    private val _dialogEventFlow = MutableSharedFlow<PromiseSettingEvent>()
    val dialogEventFlow: SharedFlow<PromiseSettingEvent> = _dialogEventFlow.asSharedFlow()

    private val _promiseUiState = MutableStateFlow(Promise())
    val promiseUiState: StateFlow<Promise> = _promiseUiState.asStateFlow()

    private val _promiseSettingUiState = MutableSharedFlow<PromiseSettingUiState>()
    val promiseSettingUiState: SharedFlow<PromiseSettingUiState> =
        _promiseSettingUiState.asSharedFlow()

    private val _networkConnection = MutableSharedFlow<Boolean>()
    val networkConnection: SharedFlow<Boolean> = _networkConnection.asSharedFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    private lateinit var myInfo: User
    private var currentMemberCodeList = listOf<String>()
    private var promiseId = ""

    init {
        viewModelScope.launch {
            userRepository.getMyInfo().first().onSuccess {
                myInfo = it
            }
        }
    }

    fun updateMember(newMemberList: List<UserUiModel>) {
        _promiseUiState.update {
            it.copy(
                members = newMemberList.map { userUiModel ->
                    userUiModel.toUser()
                }
            )
        }
    }

    fun removeMember(removeMember: UserUiModel) {
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

        if (!checkNetworkConnection()) return

        viewModelScope.launch {
            val members =promise.members.toMutableList()
            members.add(myInfo.copy(userToken = ""))
            promiseRepository.addPromise(promise.copy(members = members)).collect { result ->
                result.onSuccess { id ->
                    if (id.isEmpty()) {
                        changeUiState(PromiseSettingUiState.Fail(R.string.promiseSetting_fail))
                    } else {
                        promiseId = id
                        sendNotification()
                    }
                }
            }
        }
    }

    fun onClickPickerEditText(event: PromiseSettingEvent) {
        if (event == PromiseSettingEvent.SELECT_PLACE) {
            if (!checkNetworkConnection()) return
        }
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

    fun setPromiseDestination(destinationName: String, destinationGeoLocation: GeoLocation) {
        _promiseUiState.update {
            it.copy(
                destinationName = destinationName,
                destinationGeoLocation = destinationGeoLocation
            )
        }
    }

    fun setPromiseTitle(title: String) {
        _promiseUiState.update {
            it.copy(title = title)
        }
    }

    fun initPromise(promise: Promise) {
        _promiseUiState.update {
            val members = promise.members.filter { user -> user.userCode != myInfo.userCode }
            promise.copy(members = members)
        }
        currentMemberCodeList = _promiseUiState.value.members.map { it.userCode }
    }

    private fun changeUiState(state: PromiseSettingUiState) {
        viewModelScope.launch {
            _promiseSettingUiState.emit(state)
        }
    }

    private fun sendNotification() {
        val alarmPromise = _promiseUiState.value.copy(promiseId = promiseId)
        alarmDirector.registerAlarm(alarmPromise)

        viewModelScope.launch {
            val userCodeList =
                _promiseUiState.value.members.filter { it.userCode != myInfo.userCode }
                    .map { it.userCode }
            if ((userCodeList + currentMemberCodeList).isEmpty()) {
                _promiseUiState.update {
                    alarmPromise
                }
                changeUiState(PromiseSettingUiState.Success)
                return@launch
            }

            userRepository.getUserList(userCodeList + currentMemberCodeList).collectLatest {
                it.forEach { user ->
                    val title = if (!userCodeList.contains(user.userCode)) {
                        NotificationService.NOTIFICATION_DELETE
                    } else if (userCodeList.contains(user.userCode) &&
                        currentMemberCodeList.contains(user.userCode) &&
                        _promiseUiState.value.promiseId.isNotEmpty()
                    ) {
                        NotificationService.NOTIFICATION_EDIT
                    } else {
                        NotificationService.NOTIFICATION_ADD
                    }
                    notificationRepository.sendNotification(
                        title, _promiseUiState.value.copy(promiseId = promiseId), user.userToken
                    )
                }
                _promiseUiState.update {
                    alarmPromise
                }
                changeUiState(PromiseSettingUiState.Success)
            }
        }
    }

    private fun checkNetworkConnection(): Boolean {
        val networkConnection = runCatching {
            networkConnectionUtil.checkNetworkOnline()
        }.isSuccess
        viewModelScope.launch {
            _networkConnection.emit(networkConnection)
        }
        return networkConnection
    }

    companion object {
        const val DATE_FORMAT = "yyyy/MM/dd HH:mm"
    }

}