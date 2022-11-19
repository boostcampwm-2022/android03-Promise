package com.boosters.promise.ui.promise

import android.content.Intent
import android.os.Build.VERSION
import android.os.Bundle
import android.text.format.DateFormat
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.boosters.promise.R
import com.boosters.promise.data.place.Place
import com.boosters.promise.databinding.ActivityPromiseSettingBinding
import com.boosters.promise.ui.invite.InviteActivity
import com.boosters.promise.ui.invite.model.UserUiState
import com.boosters.promise.ui.place.PlaceSearchDialogFragment
import com.boosters.promise.ui.promise.adapter.PromiseMemberListAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PromiseSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromiseSettingBinding
    private lateinit var promiseMemberListAdapter: PromiseMemberListAdapter
    private val promiseSettingViewModel: PromiseSettingViewModel by viewModels()
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (VERSION.SDK_INT < 33) {
                    result.data?.extras?.getParcelableArrayList(MEMBER_LIST_KEY)
                } else {
                    result.data?.extras?.getParcelableArrayList(
                        MEMBER_LIST_KEY,
                        UserUiState::class.java
                    )
                }?.let {
                    promiseSettingViewModel.updateMember(it.toList())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseSettingBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.promiseSettingViewModel = promiseSettingViewModel
        setContentView(binding.root)

        promiseMemberListAdapter =
            PromiseMemberListAdapter { promiseSettingViewModel.removeMember(it) }
        binding.recyclerViewPromiseSettingPromiseMembers.adapter = promiseMemberListAdapter
        lifecycleScope.launch {
            promiseSettingViewModel.promiseUiState.collect { promiseUiState ->
                promiseMemberListAdapter.submitList(promiseUiState.members)
            }
        }

        lifecycleScope.launch {
            promiseSettingViewModel.dialogEventFlow.collectLatest { event ->
                when (event) {
                    EventType.SELECT_DATE -> showDatePicker()
                    EventType.SELECT_TIME -> showTimePicker()
                    EventType.SELECT_PLACE_SEARCH -> showPlaceSearchDialog()
                    EventType.SELECT_MEMBER -> showMember()
                }
            }
        }

    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (binding.editTextPromiseSettingPromiseTitle.hasFocus()) {
            currentFocus?.clearFocus()
            hideKeyBoard()
        }
        return super.dispatchTouchEvent(event)
    }

    private fun hideKeyBoard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            binding.editTextPromiseSettingPromiseTitle.windowToken,
            0
        )
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

    private fun showMember() {
        val members = ArrayList(promiseSettingViewModel.promiseUiState.value.members)
        val intent = Intent(this, InviteActivity::class.java).putParcelableArrayListExtra("member", members)
        getContent.launch(intent)
    }

    companion object {
        const val DATEPICKER_TAG = "New Selected Date"
        const val TIMEPICKER_TAG = "New Selected Time"
        const val SEARCH_DIALOG_TAG = "New Search Address Dialog"
        const val MEMBER_LIST_KEY = "memberList"
    }

}