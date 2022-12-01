package com.boosters.promise.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.location.LocationRepository
import com.boosters.promise.data.location.UserGeoLocation
import com.boosters.promise.data.location.toLatLng
import com.boosters.promise.data.member.Member
import com.boosters.promise.data.member.MemberRepository
import com.boosters.promise.data.notification.NotificationRepository
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.detail.model.MemberUiModel
import com.boosters.promise.ui.detail.model.PromiseUploadUiState
import com.boosters.promise.ui.notification.AlarmDirector
import com.boosters.promise.ui.notification.NotificationService
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseDetailViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository,
    private val memberRepository: MemberRepository,
    private val locationRepository: LocationRepository,
    private val notificationRepository: NotificationRepository,
    private val alarmDirector: AlarmDirector
) : ViewModel() {

    private val _promiseInfo = MutableStateFlow<Promise?>(null) //성공 , 실패
    val promiseInfo: StateFlow<Promise?> = _promiseInfo.asStateFlow()

    val promise: SharedFlow<Promise> = _promiseInfo.mapNotNull { it }.shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    lateinit var memberMarkers: List<Marker>

    val isAcceptLocationSharing = MutableStateFlow(false)

    private val _promiseUploadUiState = MutableStateFlow<PromiseUploadUiState?>(null)
    val promiseUploadUiState = _promiseUploadUiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val members: Flow<List<Member>> = promise.flatMapLatest { promise ->
        memberRepository.getMembers(promise.promiseId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val memberUiModels: Flow<List<MemberUiModel>?> = members.flatMapLatest { members ->
        userRepository.getUserList(members.map { user -> user.userCode }).flatMapLatest { users ->
            userGeoLocations.flatMapLatest { userGeoLocations ->
                flow<List<MemberUiModel>> {
                    emit(
                        users.map { user ->
                            val userGeoLocation = userGeoLocations.find { it.userCode == user.userCode }
                            val destination = promise.first().destinationGeoLocation
                            MemberUiModel(
                                userCode = user.userCode,
                                userName = user.userName,
                                isArrived = isArrival(destination, userGeoLocation)
                            )
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val userGeoLocations: SharedFlow<List<UserGeoLocation>> = members.flatMapLatest { members ->
        val locationSharingAcceptMembers = members.filter { member ->
            member.isAcceptLocation
        }.map { member ->
            member.userCode
        }
        if (locationSharingAcceptMembers.isNotEmpty()) {
            locationRepository.getGeoLocations(locationSharingAcceptMembers)
        } else {
            flow { emit(emptyList()) }
        }
    }.shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    fun setPromiseInfo(promiseId: String) {
        viewModelScope.launch {
            _promiseInfo.value = promiseRepository.getPromise(promiseId).first().copy()
            promise.collectLatest { promise ->
                memberMarkers = List(promise.members.size) { Marker() } // TODO: marker
                isAcceptLocationSharing.value = memberRepository.getIsAcceptLocation(promiseId).first().getOrElse { false }
            }
        }
    }

    fun removePromise() {
        viewModelScope.launch {
            val promiseId = promise.first().promiseId
            promiseRepository.removePromise(promiseId).collectLatest { isDeleted ->
                if (isDeleted) {
                    alarmDirector.removeAlarm(promiseId)
                    sendNotification()
                } else {
                    _isDeleted.value = false
                }
            }
        }
    }

    fun updateLocationSharingPermission(isAcceptLocationSharing: Boolean) {
        viewModelScope.launch {
            try {
                val userCode = userRepository.getMyInfo().first().getOrElse { throw IllegalStateException() }.userCode
                promise.collectLatest { promise ->
                    _promiseUploadUiState.value = if (isAcceptLocationSharing) {
                        PromiseUploadUiState.Accept(
                            id = promise.promiseId,
                            dateAndTime = "${promise.date} ${promise.time}"
                        )
                    } else {
                        PromiseUploadUiState.Denied(
                            id = promise.promiseId,
                        )
                    }

                    memberRepository.updateIsAcceptLocation(
                        Member(
                            promiseId = promise.promiseId,
                            userCode = userCode,
                            isAcceptLocation = isAcceptLocationSharing
                        )
                    )
                }
            } catch (e: IllegalStateException) {
                cancel()
            }
        }
    }

    private fun sendNotification() {
        viewModelScope.launch {
            userRepository.getMyInfo().first().onSuccess { myInfo ->
                val userCodeList =
                    _promiseInfo.value?.members?.filter { it.userCode != myInfo.userCode }
                        ?.map { it.userCode }
                if (userCodeList != null && userCodeList.isEmpty()) {
                    return@launch
                }

                if (userCodeList != null) {
                    userRepository.getUserList(userCodeList).collectLatest {
                        it.forEach { user ->
                            _promiseInfo.value?.let { promise ->
                                notificationRepository.sendNotification(
                                    NotificationService.NOTIFICATION_DELETE,
                                    promise,
                                    user.userToken,
                                )
                            }
                        }
                    }
                }
                _isDeleted.value = true
            }
        }
    }

    fun isArrival(destination: GeoLocation, userGeoLocation: UserGeoLocation?): Boolean {
        val distance = userGeoLocation?.geoLocation?.let { calculateDistance(destination, it) }
        if (distance != null) {
            return distance < MINIMUM_ARRIVE_DISTANCE
        }
        return false
    }

    private fun calculateDistance(location1: GeoLocation, location2: GeoLocation): Double {
        return location1.toLatLng().distanceTo(location2.toLatLng())
    }

    companion object {
        private const val MINIMUM_ARRIVE_DISTANCE = 50
    }

}