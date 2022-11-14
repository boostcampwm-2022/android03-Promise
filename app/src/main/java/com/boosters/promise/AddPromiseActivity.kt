package com.boosters.promise

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.boosters.promise.databinding.ActivityAddPromiseBinding
import java.util.*

class AddPromiseActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityAddPromiseBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_add_promise)

        val calendar = Calendar.getInstance()
        binding.buttonAddPromiseSelectDate.setOnClickListener(selectDateButtonListener(calendar))


    }

    private fun selectDateButtonListener(cal: Calendar) = View.OnClickListener {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            binding.buttonAddPromiseSelectDate.text = "%d/%d/%d".format(year, month + 1, day)
        }

        DatePickerDialog(
            this,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}