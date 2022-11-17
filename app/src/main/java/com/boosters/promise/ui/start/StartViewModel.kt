package com.boosters.promise.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.source.remote.PromiseRepository
import com.boosters.promise.data.promise.toPromiseUiState
import com.boosters.promise.ui.model.PromiseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _promiseList = MutableLiveData<MutableList<PromiseUiState>?>()
    val promiseList: LiveData<MutableList<PromiseUiState>?> get() = _promiseList

    fun getPromiseList(date: String) {
        viewModelScope.launch {
            val promiseList = mutableListOf<PromiseUiState>()
            promiseRepository.getPromiseList(date).forEach {
                promiseList.add(it.toPromiseUiState())
            }
            _promiseList.setValue(promiseList)
        }
    }

}
