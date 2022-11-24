package com.boosters.promise.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.ui.promisesetting.model.PromiseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseDetailViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
) : ViewModel() {

    private var _promiseInfo = MutableLiveData<PromiseUiState>()
    val promiseInfo: LiveData<PromiseUiState> = _promiseInfo

    private var _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    fun setPromiseInfo(promise: PromiseUiState) {
        _promiseInfo.value = promise
    }

    fun removePromise() {
        viewModelScope.launch {
            _promiseInfo.value?.let {
                promiseRepository.removePromise(it.promiseId).collectLatest {
                    _isDeleted.value = it
                }
            }
        }
    }

}