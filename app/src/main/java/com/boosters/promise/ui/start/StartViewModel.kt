package com.boosters.promise.ui.start

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.promise.PromiseResponseBody
import com.boosters.promise.data.promise.source.remote.PromiseRepository
import com.boosters.promise.data.user.User
import com.boosters.promise.ui.model.Promise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _promiseList = MutableLiveData<MutableList<Promise>?>()
    val promiseList: LiveData<MutableList<Promise>?> get() = _promiseList

    fun getPromiseList(date: String) {
        viewModelScope.launch {
            val promiseList = promiseRepository.getPromiseList(date)
            _promiseList.setValue(promiseList)
        }
    }

}
