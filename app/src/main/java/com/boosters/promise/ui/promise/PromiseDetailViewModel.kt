package com.boosters.promise.ui.promise

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.ui.invite.model.UserUiState
import com.boosters.promise.ui.promise.model.PromiseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseDetailViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
) : ViewModel() {

    private val _promiseUiState = MutableStateFlow(PromiseUiState(
        promiseId = "0fw9AjLvoKLBZWrNdsrK",
        title = "데이트",
        date = "2022/11/13"
    ))

    val promiseUiState: StateFlow<PromiseUiState> = _promiseUiState.asStateFlow()

    private var _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    fun removePromise() {
        viewModelScope.launch {
            promiseRepository.removePromise(_promiseUiState.value.promiseId).collectLatest {
                _isDeleted.value = it
            }
        }
    }

}