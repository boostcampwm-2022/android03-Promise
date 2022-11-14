package com.boosters.promise

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.boosters.promise.databinding.DialogSearchAddressBinding

class SearchAddressDialogFragment : DialogFragment() {

    private lateinit var listener: SearchAddressDialogListener

    interface SearchAddressDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, result: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    private lateinit var _binding: DialogSearchAddressBinding
    private val binding get() = _binding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            var searchResult = ""

            _binding =
                DataBindingUtil.inflate(inflater, R.layout.dialog_search_address, null, false)

            binding.searchViewDialogSearchAddress.apply {
                isSubmitButtonEnabled = true
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        searchResult = query.orEmpty()

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