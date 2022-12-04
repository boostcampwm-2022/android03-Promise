package com.boosters.promise.ui.detail

import androidx.lifecycle.*
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
import com.boosters.promise.ui.detail.model.MemberMarkerInfo
import com.boosters.promise.ui.detail.model.MemberUiModel
import com.boosters.promise.ui.detail.model.PromiseUploadUiState
import com.boosters.promise.ui.notification.AlarmDirector
import com.boosters.promise.ui.notification.NotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PromiseDetailViewModel @AssistedInject constructor(
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository,
    private val memberRepository: MemberRepository,
    private val locationRepository: LocationRepository,
    private val notificationRepository: NotificationRepository,
    private val alarmDirector: AlarmDirector,
    @Assisted promiseId: String
) : ViewModel() {

    val promise: SharedFlow<Promise> = promiseRepository.getPromise(promiseId).shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    val isAcceptLocationSharing = memberRepository.getIsAcceptLocation(promiseId)

    private val _promiseUploadUiState = MutableStateFlow<PromiseUploadUiState?>(null)
    val promiseUploadUiState = _promiseUploadUiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val members: Flow<List<Member>> = promise.flatMapLatest { promise ->
        memberRepository.getMembers(promise.promiseId)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val memberUiModels: Flow<List<MemberUiModel>?> = userGeoLocations.flatMapLatest { userGeoLocations ->
        flow<List<MemberUiModel>> {
            val memberUiModel = promise.first().members.map { user ->
                val userGeoLocation = userGeoLocations.find { it.userCode == user.userCode }
                val destination = promise.first().destinationGeoLocation
                MemberUiModel(
                    userCode = user.userCode,
                    userName = user.userName,
                    isArrived = isArrival(destination, userGeoLocation)
                )
            }
            emit(memberUiModel)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val memberMarkerInfo: SharedFlow<List<MemberMarkerInfo>> = userGeoLocations.mapLatest { userGeoLocations ->
        userGeoLocations.mapNotNull { userGeoLocation ->
            MemberMarkerInfo(
                id = userGeoLocation.userCode,
                name = promise.first().members.find { it.userCode == userGeoLocation.userCode }?.userName ?: "",
                geoLocation = userGeoLocation.geoLocation ?: return@mapNotNull null
            )
        }
    }.shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    fun removePromise() {
        viewModelScope.launch {
            val promise = promise.first()
            val isDeleteSuccess = promiseRepository.removePromise(promise.promiseId).first()
            if (isDeleteSuccess) {
                alarmDirector.removeAlarm(promise.promiseId)
                sendDeleteNotification(promise)
            }
            _isDeleted.value = isDeleteSuccess
        }
    }

    private suspend fun sendDeleteNotification(promise: Promise) {
        userRepository.getMyInfo().first().onSuccess { myInfo ->
            val users = promise.members.filter { it.userCode != myInfo.userCode }
            if (users.isEmpty()) return

            users.forEach { user ->
                notificationRepository.sendNotification(
                    NotificationService.NOTIFICATION_DELETE,
                    promise,
                    user.userToken,
                )
            }
        }
    }

    fun updateLocationSharingPermission(isAcceptLocationSharing: Boolean) {
        viewModelScope.launch {
            val userCode = userRepository.getMyInfo().first().getOrElse { return@launch }.userCode
            val promise = promise.first()
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
    }

    private fun isArrival(destination: GeoLocation, userGeoLocation: UserGeoLocation?): Boolean {
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

        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: PromiseDetailViewModelFactory,
            promiseId: String
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PromiseDetailViewModel::class.java)) {
                    return assistedFactory.create(promiseId) as T
                }
                throw IllegalArgumentException()
            }
        }
    }

    @AssistedFactory
    interface PromiseDetailViewModelFactory {

        fun create(promiseId: String): PromiseDetailViewModel

    }

}