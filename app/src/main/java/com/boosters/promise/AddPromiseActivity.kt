package com.boosters.promise

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.boosters.promise.databinding.ActivityAddPromiseBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddPromiseActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityAddPromiseBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_add_promise)

        val calendar = Calendar.getInstance()
        binding.buttonAddPromiseSelectDate.setOnClickListener(selectDateListener(calendar))
        binding.buttonAddPromiseSelectTime.setOnClickListener(selectTimeListener(calendar))


    }

    private fun selectDateListener(cal: Calendar) = View.OnClickListener {
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

    private fun selectTimeListener(cal: Calendar) = View.OnClickListener {

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(cal.get(Calendar.HOUR))
            .setMinute(cal.get(Calendar.MINUTE))
            .setInputMode(INPUT_MODE_CLOCK)
            .setTitleText("약속 시간을 설정하세요.")
            .build()

        timePicker.show(supportFragmentManager, "tag")
        timePicker.addOnPositiveButtonClickListener {
            binding.buttonAddPromiseSelectTime.text =
                "%d:%d".format(timePicker.hour, timePicker.minute)
        }
    }
}