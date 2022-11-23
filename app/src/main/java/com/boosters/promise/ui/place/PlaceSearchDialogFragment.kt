package com.boosters.promise.ui.place

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.boosters.promise.R
import com.boosters.promise.data.place.Place
import com.boosters.promise.databinding.DialogPlaceSearchBinding
import com.boosters.promise.ui.place.adapter.PlaceSearchAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaceSearchDialogFragment : DialogFragment() {

    private var _binding: DialogPlaceSearchBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val placeSearchViewModel: PlaceSearchViewModel by viewModels()

    private lateinit var onClickListener: (Place) -> Unit

    @OptIn(FlowPreview::class)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = MaterialAlertDialogBuilder(activity)
            val inflater = requireActivity().layoutInflater

            _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_place_search, null, false)

            val placeSearchAdapter = PlaceSearchAdapter { item ->
                onClickListener(item)
                dismiss()
            }
            binding.recyclerViewDialogPlaceSearchResult.adapter = placeSearchAdapter
            val decoration = DividerItemDecoration(context, VERTICAL)
            binding.recyclerViewDialogPlaceSearchResult.addItemDecoration(decoration)

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    placeSearchViewModel.promiseUiState.collect { searchResult ->
                        placeSearchAdapter.setDataSet(searchResult)
                    }
                }
            }

            binding.searchViewDialogPlaceSearch.textChangesToFlow()
                .filterNot { it.isNullOrEmpty() }
                .debounce(PlaceSearchViewModel.SEARCH_TERM)
                .distinctUntilChanged()
                .onEach { query ->
                    placeSearchViewModel.searchPlace(checkNotNull(query))
                }
                .launchIn(lifecycleScope)

            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setOnSelectPlaceSearchListener(onClickListener: (Place) -> Unit): DialogFragment {
        this.onClickListener = onClickListener
        return this@PlaceSearchDialogFragment
    }

    private fun SearchView.textChangesToFlow(): Flow<String?> {
        return callbackFlow {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(query: String?): Boolean {
                    trySend(query)
                    return false
                }
            })
            awaitClose { setOnQueryTextListener(null) }
        }.onStart { emit(query.toString()) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}