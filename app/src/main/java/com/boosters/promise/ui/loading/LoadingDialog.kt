package com.boosters.promise.ui.loading

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.DialogLoadingProgressBinding

class LoadingDialog(
    context: Context,
    private val message: String = context.getString(R.string.dialogLoading_default_message)
) : Dialog(context) {

    private lateinit var binding: DialogLoadingProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_loading_progress, null, false)
        setContentView(binding.root)

        binding.message = message
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}