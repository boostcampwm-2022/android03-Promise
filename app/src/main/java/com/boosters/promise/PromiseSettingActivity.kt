package com.boosters.promise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.boosters.promise.databinding.ActivityPromiseSettingBinding
import com.boosters.promise.network.LocalResponse
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PromiseSettingActivity : AppCompatActivity() {

    private var binding: ActivityPromiseSettingBinding? = null
    private val viewModel: PromiseSettingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseSettingBinding.inflate(layoutInflater)
        binding?.lifecycleOwner = this
        binding?.promiseSettingViewModel = viewModel
        setContentView(binding?.root)
        setObserver()

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        binding?.appCompatEditTextPromiseSettingPromiseDate?.setOnClickListener(selectDateListener(calendar))
        binding?.appCompatEditTextPromiseSettingPromiseTime?.setOnClickListener(selectTimeListener(calendar))
        binding?.appCompatEditTextPromiseSettingPromisePlace?.setOnClickListener(searchAddressListener())

    }

    private fun setObserver() {
        viewModel.promiseMemberList.observe(this) {
            val chipGroup = binding?.chipGroupPromiseSetting
            val children = it?.mapIndexed { index, user ->
                val chip = LayoutInflater.from(chipGroup?.context)
                    .inflate(R.layout.item_promise_member, chipGroup, false) as Chip
                chip.isCheckable = false
                chip.text = user.userName
                chip.setOnCloseIconClickListener {
                    viewModel.removeMember(index)
                }
                chip
            }
            chipGroup?.removeAllViews()
            children?.forEach { chip ->
                chipGroup?.addView(chip)
            }
        }
    }

    fun selectDateListener(cal: Calendar) = View.OnClickListener {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(cal.timeInMillis)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTitleText(getString(R.string.title_datepicker))
            .build()

        datePicker.show(supportFragmentManager, DATEPICKER_TAG)
        datePicker.addOnPositiveButtonClickListener {
            cal.timeInMillis = datePicker.selection ?: cal.timeInMillis
        }
    }

    fun selectTimeListener(cal: Calendar) = View.OnClickListener {
        val isSystem24Hour = DateFormat.is24HourFormat(this)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(cal.get(Calendar.HOUR))
            .setMinute(cal.get(Calendar.MINUTE))
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTitleText(getString(R.string.title_timepicker))
            .build()

        timePicker.show(supportFragmentManager, TIMEPICKER_TAG)
        timePicker.addOnPositiveButtonClickListener {
            cal.set(Calendar.HOUR, timePicker.hour)
            cal.set(Calendar.MINUTE, timePicker.minute)
        }
    }

    fun searchAddressListener() = View.OnClickListener {
        SearchAddressDialogFragment()
            .setOnSearchAddressDialogListener(object :
                SearchAddressDialogFragment.SearchAddressDialogListener {
                override fun onDialogPositiveClick(dialog: DialogFragment, result: LocalResponse) {
                    val items = result.items

                    if (items.isNotEmpty()) {
                        items.first()?.let { firstItem ->

                        }
                    } else {
                    }
                }

                override fun onDialogNegativeClick(dialog: DialogFragment) {
                    binding?.textViewPromiseSettingPromisePlace?.text = getString(R.string.search_address)
                }
            })
            .show(supportFragmentManager, SEARCH_DIALOG_TAG)
    }

    companion object {
        const val DATEPICKER_TAG = "New Selected Date"
        const val TIMEPICKER_TAG = "New Selected Time"
        const val SEARCH_DIALOG_TAG = "New Search Address Dialog"
    }

}