package com.boosters.promise.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.place.Place
import com.boosters.promise.data.place.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    private val placeRepository: PlaceRepository
) : ViewModel() {

    private val _placeUiState = MutableStateFlow<List<Place>>(mutableListOf())
    val promiseUiState: StateFlow<List<Place>> = _placeUiState.asStateFlow()

    fun searchPlace(query: String) {
        viewModelScope.launch {
            placeRepository.searchPlace(query).onSuccess { searchResult ->
                _placeUiState.value = searchResult
            }
        }
    }

    companion object {
        const val SEARCH_TERM = 500L
    }

}