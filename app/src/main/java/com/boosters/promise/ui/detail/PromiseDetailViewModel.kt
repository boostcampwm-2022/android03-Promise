package com.boosters.promise.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.location.toLatLng
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.data.user.toMemberUiModel
import com.boosters.promise.ui.detail.model.MemberUiModel
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseDetailViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _promiseInfo = MutableStateFlow(Promise())
    val promiseInfo: StateFlow<Promise> get() = _promiseInfo.asStateFlow()

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    lateinit var memberMarkers: List<Marker>

    @OptIn(ExperimentalCoroutinesApi::class)
    val memberLocations = promiseInfo.flatMapLatest { promise ->
        userRepository.getUserList(promise.members.map { user -> user.userCode })
    }.map {
        it.map { user ->
            user.toMemberUiModel()
        }
    }

    fun setPromiseInfo(promiseId: String) {
        viewModelScope.launch {
            _promiseInfo.value = promiseRepository.getPromise(promiseId).first().copy()
            memberMarkers = List(_promiseInfo.value.members.size) { Marker() }
        }
    }

    fun removePromise() {
        viewModelScope.launch {
            _promiseInfo.value.let {
                promiseRepository.removePromise(it.promiseId).collectLatest { isDeleted ->
                    _isDeleted.value = isDeleted
                }
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