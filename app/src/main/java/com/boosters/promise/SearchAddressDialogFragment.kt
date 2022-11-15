package com.boosters.promise

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.boosters.promise.databinding.DialogSearchAddressBinding
import com.boosters.promise.network.LocalResponse
import com.boosters.promise.network.Retrofit.promiseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchAddressDialogFragment : DialogFragment() {

    private lateinit var listener: SearchAddressDialogListener
    private lateinit var searchResult: LocalResponse

    interface SearchAddressDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, result: LocalResponse)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    private lateinit var _binding: DialogSearchAddressBinding
    private val binding get() = _binding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            _binding =
                DataBindingUtil.inflate(inflater, R.layout.dialog_search_address, null, false)

            val adapter = SearchAddressListAdapter()
            binding.listViewDialogSearchAddressResult.adapter = adapter

            binding.searchViewDialogSearchAddress.apply {
                isSubmitButtonEnabled = true
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {

                        CoroutineScope(Dispatchers.IO).launch {
                            searchResult = promiseService.searchLocalQuery(query.orEmpty(), 5)
                            withContext(Dispatchers.Main) {
                                adapter.submitList(searchResult.items)
                            }
                        }

                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean = false
                })
            }

            builder.setView(binding.root)
                .setPositiveButton(R.string.ok) { _, _ ->
                    listener.onDialogPositiveClick(this, searchResult)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    listener.onDialogNegativeClick(this)
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setOnSearchAddressDialogListener(listenerImpl: SearchAddressDialogListener): DialogFragment {
        listener = listenerImpl
        return this@SearchAddressDialogFragment
    }
}