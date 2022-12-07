package com.boosters.promise.ui.promisecalendar

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.network.NetworkConnectionUtil
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.promisecalendar.model.PromiseListUiState
import com.boosters.promise.ui.promisecalendar.model.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PromiseCalendarViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val promiseRepository: PromiseRepository,
    private val networkConnectionUtil: NetworkConnectionUtil
) : ViewModel() {

    private val _myInfo: MutableStateFlow<UserUiState> = MutableStateFlow(UserUiState.Loading)
    private val myInfo: StateFlow<UserUiState> =
        _myInfo.stateIn(viewModelScope, SharingStarted.Eagerly, UserUiState.Loading)

    private val _myPromiseList: MutableStateFlow<PromiseListUiState> =
        MutableStateFlow(PromiseListUiState.Loading)
    val myPromiseList: StateFlow<PromiseListUiState> =
        _myPromiseList.stateIn(viewModelScope, SharingStarted.Eagerly, PromiseListUiState.Empty)

    private val _dailyPromiseList: MutableStateFlow<List<Promise>?> = MutableStateFlow(null)
    val dailyPromiseList: StateFlow<List<Promise>?> get() = _dailyPromiseList.asStateFlow()

    private val _networkConnection = MutableSharedFlow<Boolean>()
    val networkConnection: SharedFlow<Boolean> = _networkConnection.asSharedFlow()

    private val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    init {
        loadMyInfo()
        loadPromiseList()
    }

    private fun loadMyInfo() {
        viewModelScope.launch {
            _myInfo.emit(UserUiState.Loading)
            userRepository.getMyInfo().collectLatest { result ->
                result.onSuccess { myInfo ->
                    _myInfo.emit(UserUiState.Success(myInfo))
                }
            }
        }
    }

    private fun loadPromiseList() {
        checkNetworkConnection()
        viewModelScope.launch {
            _myPromiseList.emit(PromiseListUiState.Loading)
            myInfo.collectLatest { myInfo ->
                if (myInfo is UserUiState.Success) {
                    promiseRepository.getPromiseList(myInfo.data).collectLatest { promiseList ->
                        _myPromiseList.emit(PromiseListUiState.Success(promiseList))
                    }
                }
            }
        }
    }

    fun updateDailyPromiseList(date: String) {
        checkNetworkConnection()
        viewModelScope.launch {
            _myPromiseList.collectLatest {
                if (it is PromiseListUiState.Success) {
                    _dailyPromiseList.value = it.data.filter { promise ->
                        promise.date == date
                    }.sortedBy { promise ->
                        dateFormatter.parse(promise.time).time
                    }
                }
            }
        }
    }

    fun checkNetworkConnection() {
        val networkConnection = runCatching {
            networkConnectionUtil.checkNetworkOnline()
        }.isSuccess
        viewModelScope.launch {
            _networkConnection.emit(networkConnection)
        }
    }

    companion object {
        private const val DATE_FORMAT = "HH:mm"
    }

}