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
import com.boosters.promise.ui.place.model.toPlace
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaceSearchDialogFragment : DialogFragment() {

    private lateinit var _binding: DialogPlaceSearchBinding
    private val binding get() = _binding
    private val placeSearchViewModel: PlaceSearchViewModel by viewModels()

    private lateinit var onClickListener: (Place) -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater

            _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_place_search, null, false)

            val placeSearchAdapter = PlaceSearchAdapter { item ->
                onClickListener(item.toPlace())
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

            val searchQueryFlow = binding.searchViewDialogPlaceSearch.textChangesToFlow()
            placeSearchViewModel.searchPlace(searchQueryFlow)

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

}