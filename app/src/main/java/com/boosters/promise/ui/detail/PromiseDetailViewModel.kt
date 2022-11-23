package com.boosters.promise.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boosters.promise.ui.promisesetting.model.PromiseUiState

class PromiseDetailViewModel : ViewModel() {

    private var _promiseInfo = MutableLiveData<PromiseUiState>()
    val promiseInfo: LiveData<PromiseUiState> = _promiseInfo

    fun setPromiseInfo(promise: PromiseUiState) {
        _promiseInfo.value = promise
    }

}