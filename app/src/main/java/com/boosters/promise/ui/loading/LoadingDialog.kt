package com.boosters.promise.ui.loading

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.boosters.promise.R

class LoadingDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)

            builder.setView(R.layout.dialog_loading_progress)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}