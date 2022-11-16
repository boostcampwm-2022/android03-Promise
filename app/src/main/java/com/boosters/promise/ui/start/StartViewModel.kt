package com.boosters.promise.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.PromiseState
import com.boosters.promise.data.promise.source.remote.PromiseRepository
import com.boosters.promise.ui.model.PromiseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _promiseList = MutableLiveData<MutableList<PromiseState>?>()
    val promiseList: LiveData<MutableList<PromiseState>?> get() = _promiseList

    fun getPromiseList(date: String) {
        viewModelScope.launch {
            val promiseList = mutableListOf<PromiseState>()
            promiseRepository.getPromiseList(date).forEach {
                promiseList.add(it.PromiseState())
            }
            _promiseList.setValue(promiseList)
        }
    }

}
