package com.boosters.promise.ui.promisecalendar

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.boosters.promise.util.NetworkConnectionUtil
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.promisecalendar.model.PromiseListUiState
import com.boosters.promise.ui.promisecalendar.model.UserUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class PromiseCalendarViewModel @AssistedInject constructor(
    private val userRepository: UserRepository,
    private val promiseRepository: PromiseRepository,
    private val networkConnectionUtil: NetworkConnectionUtil,
    @Assisted val today: String
) : ViewModel() {

    private val _myInfo: MutableStateFlow<UserUiState> = MutableStateFlow(UserUiState.Loading)
    private val myInfo: StateFlow<UserUiState> =
        _myInfo.stateIn(viewModelScope, SharingStarted.Eagerly, UserUiState.Loading)

    private val _myPromiseList: MutableStateFlow<PromiseListUiState> =
        MutableStateFlow(PromiseListUiState.Loading)
    val myPromiseList: StateFlow<PromiseListUiState> =
        _myPromiseList.stateIn(viewModelScope, SharingStarted.Eagerly, PromiseListUiState.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val dailyPromiseList: Flow<List<Promise>?> get() = selectedDate.flatMapLatest { date ->
        myPromiseList.map { promiseListUiState ->
            if (promiseListUiState is PromiseListUiState.Success) {
                return@map promiseListUiState.data
                    .filter { promise: Promise -> promise.date == date }
                    .sortedBy { promise ->
                        dateFormatter.parse(promise.time).time
                    }
            }
            null
        }
    }

    private val _networkConnection = MutableSharedFlow<Boolean>()
    val networkConnection: SharedFlow<Boolean> = _networkConnection.asSharedFlow()

    private val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    private val selectedDate = MutableStateFlow(today)

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

    fun selectDate(date: String) {
        checkNetworkConnection()
        selectedDate.value = date
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

        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: PromiseCalendarModelFactory,
            today: String
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PromiseCalendarViewModel::class.java)) {
                    return assistedFactory.create(today) as T
                }
                throw IllegalArgumentException()
            }
        }
    }

    @AssistedFactory
    interface PromiseCalendarModelFactory {

        fun create(today: String): PromiseCalendarViewModel

    }

}