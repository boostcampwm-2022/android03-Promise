package com.boosters.promise.ui.promisecalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.promisecalendar.model.PromiseListUiState
import com.boosters.promise.ui.promisecalendar.model.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseCalendarViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _myInfo: MutableStateFlow<UserUiState> = MutableStateFlow(UserUiState.Loading)
    private val myInfo: StateFlow<UserUiState> =
        _myInfo.stateIn(viewModelScope, SharingStarted.Eagerly, UserUiState.Loading)

    private val _myPromiseList: MutableStateFlow<PromiseListUiState> =
        MutableStateFlow(PromiseListUiState.Loading)
    val myPromiseList: SharedFlow<PromiseListUiState> =
        _myPromiseList.stateIn(viewModelScope, SharingStarted.Eagerly, PromiseListUiState.Empty)

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

}