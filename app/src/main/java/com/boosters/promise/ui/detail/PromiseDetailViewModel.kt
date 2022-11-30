package com.boosters.promise.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.location.toLatLng
import com.boosters.promise.data.location.LocationRepository
import com.boosters.promise.data.location.UserGeoLocation
import com.boosters.promise.data.member.Member
import com.boosters.promise.data.member.MemberRepository
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.detail.model.MemberUiModel
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
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _promiseInfo = MutableStateFlow<Promise?>(null)
    val promiseInfo: StateFlow<Promise?> get() = _promiseInfo.asStateFlow()

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    lateinit var memberMarkers: List<Marker>

    val isAcceptLocationSharing = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val memberLocations: Flow<List<UserGeoLocation?>?> = promiseInfo.flatMapLatest { promise ->
        if (promise != null) {
            memberRepository.getMembers(promise.promiseId).flatMapLatest { members ->
                locationRepository.getGeoLocations(
                    members.filter { member ->
                        member.isAcceptLocation
                    }.map { member -> member.userCode }
                )
            }
        } else {
            flow<List<UserGeoLocation?>?> { emit(null) }
        }
    }

    fun setPromiseInfo(promiseId: String) {
        viewModelScope.launch {
            _promiseInfo.value = promiseRepository.getPromise(promiseId).first().copy()
            promiseInfo.collectLatest { promise ->
                if (promise != null) {
                    memberMarkers = List(promise.members.size) { Marker() }
                    isAcceptLocationSharing.value = memberRepository.getIsAcceptLocation(promiseId).first().getOrElse { false }
                    cancel()
                }
            }
        }
    }

    fun removePromise() {
        viewModelScope.launch {
            promiseInfo.value.let { promise ->
                if (promise != null) {
                    promiseRepository.removePromise(promise.promiseId).collectLatest { isDeleted ->
                        _isDeleted.value = isDeleted
                        cancel()
                    }
                }
            }
        }
    }

    fun updateLocationSharingPermission(isAcceptLocationSharing: Boolean) {
        viewModelScope.launch {
            try {
                val userCode = userRepository.getMyInfo().first().getOrElse { throw IllegalStateException() }.userCode
                promiseInfo.collectLatest { promise ->
                    if (promise != null) {
                        memberRepository.updateIsAcceptLocation(
                            Member(
                                promiseId = promise.promiseId,
                                userCode = userCode,
                                isAcceptLocation = isAcceptLocationSharing
                            )
                        )
                        cancel()
                    }
                }
            } catch (e: IllegalStateException) {
                cancel()
            }
        }
    }

    fun checkArrival(destination: GeoLocation, members: List<MemberUiModel>) =
        members.map { member ->
            if (member.geoLocation != null) {
                val distance = calculateDistance(destination, member.geoLocation)

                if (distance < MINIMUM_ARRIVE_DISTANCE) {
                    member.copy(isArrived = true)
                } else {
                    member.copy(isArrived = false)
                }
            } else {
                member.copy(isArrived = false)
            }
        }

    private fun calculateDistance(location1: GeoLocation, location2: GeoLocation): Double {
        return location1.toLatLng().distanceTo(location2.toLatLng())
    }

    companion object {
        private const val MINIMUM_ARRIVE_DISTANCE = 50
    }

}