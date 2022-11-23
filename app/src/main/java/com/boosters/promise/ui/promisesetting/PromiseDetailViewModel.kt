package com.boosters.promise.ui.promisesetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.ui.promisesetting.model.PromiseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseDetailViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
) : ViewModel() {

    private val _promiseUiState = MutableStateFlow(
        PromiseUiState(
        promiseId = "0zsFeTftSqdmNCgMIwJr",
        title = "데이트",
        date = "2022/11/13"
    )
    )

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