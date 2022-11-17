package com.boosters.promise.ui.promise

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.boosters.promise.R
import com.boosters.promise.data.place.Place
import com.boosters.promise.databinding.ActivityPromiseSettingBinding
import com.boosters.promise.ui.place.PlaceSearchDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class PromiseSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromiseSettingBinding
    private val promiseSettingViewModel: PromiseSettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseSettingBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.promiseSettingViewModel = promiseSettingViewModel
        setContentView(binding.root)
        setObserver()

        lifecycleScope.launch {
            promiseSettingViewModel.dialogEventFlow.collectLatest { event ->
                when (event) {
                    Event.ShowDatePicker -> showDatePicker()
                    Event.ShowTimePicker -> showTimePicker()
                    Event.ShowPlaceSearchDialog -> showPlaceSearchDialog()
                }
            }
        }

    }

    private fun setObserver() {
        promiseSettingViewModel.promiseMemberList.observe(this) {
            val chipGroup = binding.chipGroupPromiseSetting
            val children = it?.mapIndexed { index, user ->
                val chip = LayoutInflater.from(chipGroup.context)
                    .inflate(R.layout.item_promise_member, chipGroup, false) as Chip
                chip.isCheckable = false
                chip.text = user.userName
                chip.setOnCloseIconClickListener {
                    promiseSettingViewModel.removeMember(index)
                }
                chip
            }
            chipGroup.removeAllViews()
            children?.forEach { chip ->
                chipGroup.addView(chip)
            }
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(cal.timeInMillis)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTitleText(getString(R.string.title_datepicker))
            .build()

        datePicker.show(supportFragmentManager, DATEPICKER_TAG)
        datePicker.addOnPositiveButtonClickListener {
            cal.timeInMillis = datePicker.selection ?: cal.timeInMillis
            promiseSettingViewModel.setPromiseDate(
                getString(R.string.date_format).format(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
            )
        }
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
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
            promiseSettingViewModel.setPromiseTime(
                getString(R.string.time_format).format(
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE)
                )
            )
        }
    }

    private fun showPlaceSearchDialog() {
        PlaceSearchDialogFragment()
            .setOnSearchPlaceDialogListener(object :
                PlaceSearchDialogFragment.SearchAddressDialogListener {
                override fun onDialogPositiveClick(dialog: DialogFragment, resultItem: Place?) {
                    promiseSettingViewModel.setPromiseDestination(resultItem?.placeTitle.orEmpty())
                }

                override fun onDialogNegativeClick(dialog: DialogFragment) {}
            })
            .show(supportFragmentManager, SEARCH_DIALOG_TAG)
    }

    companion object {
        const val DATEPICKER_TAG = "New Selected Date"
        const val TIMEPICKER_TAG = "New Selected Time"
        const val SEARCH_DIALOG_TAG = "New Search Address Dialog"
    }

}