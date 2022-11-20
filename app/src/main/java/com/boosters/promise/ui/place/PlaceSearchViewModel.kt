package com.boosters.promise.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.data.place.PlaceRepository
import com.boosters.promise.data.place.toPlaceUiState
import com.boosters.promise.ui.place.model.PlaceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PlaceSearchViewModel @Inject constructor(
    private val placeRepository: PlaceRepository
) : ViewModel() {

    private val _placeUiState = MutableStateFlow<List<PlaceUiState>>(mutableListOf())
    val promiseUiState: StateFlow<List<PlaceUiState>> = _placeUiState.asStateFlow()

    @OptIn(FlowPreview::class)
    fun searchPlace(queryFlow: Flow<String?>) {
        queryFlow.filterNot { it.isNullOrEmpty() }
            .debounce(SEARCH_TERM)
            .distinctUntilChanged()
            .onEach { query ->
                placeRepository.searchPlace(query.orEmpty()).onSuccess { searchResult ->
                    _placeUiState.value = searchResult.map {
                        it.toPlaceUiState()
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    companion object {
        const val SEARCH_TERM = 500L
    }

}