package com.boosters.promise.ui.place

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.data.place.Place
import com.boosters.promise.databinding.DialogPlaceSearchBinding
import com.boosters.promise.ui.place.adapter.PlaceSearchListAdapter
import com.boosters.promise.ui.promise.model.toPlace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaceSearchDialogFragment : DialogFragment() {

    private lateinit var _binding: DialogPlaceSearchBinding
    private val binding get() = _binding
    private val placeSearchViewModel: PlaceSearchViewModel by viewModels()

    private lateinit var listener: SearchAddressDialogListener
    private var selectedPlace: Place? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_place_search, null, false)

            val placeSearchListAdapter = PlaceSearchListAdapter { item ->
                binding.searchViewDialogSearchAddress.setQuery(item.title, false)
                selectedPlace = item.toPlace()
            }
            binding.listViewDialogSearchAddressResult.adapter = placeSearchListAdapter

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    placeSearchViewModel.promiseUiState.collect { searchResult ->
                        placeSearchListAdapter.submitList(searchResult)
                    }
                }
            }

            binding.searchViewDialogSearchAddress.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    placeSearchViewModel.searchPlace(query.orEmpty())
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })

            builder.setView(binding.root)
                .setPositiveButton(R.string.ok) { _, _ ->
                    listener.onDialogPositiveClick(this, selectedPlace)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    listener.onDialogNegativeClick(this)
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setOnSearchPlaceDialogListener(listenerImpl: SearchAddressDialogListener): DialogFragment {
        listener = listenerImpl
        return this@PlaceSearchDialogFragment
    }

    interface SearchAddressDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, resultItem: Place?)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

}