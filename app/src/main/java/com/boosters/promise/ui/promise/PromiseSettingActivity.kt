package com.boosters.promise.ui.promise

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityPromiseSettingBinding
import com.boosters.promise.ui.invite.InviteActivity
import com.boosters.promise.ui.invite.model.UserUiState
import com.boosters.promise.ui.place.PlaceSearchDialogFragment
import com.boosters.promise.ui.promise.adapter.PromiseMemberListAdapter
import com.boosters.promise.ui.promise.model.PromiseSettingEvent
import com.boosters.promise.ui.promise.model.PromiseSettingUiState
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

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
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseSettingBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.promiseSettingViewModel = promiseSettingViewModel
        setContentView(binding.root)

        askNotificationPermission()

        promiseMemberListAdapter =
            PromiseMemberListAdapter { promiseSettingViewModel.removeMember(it) }
        binding.recyclerViewPromiseSettingPromiseMembers.adapter = promiseMemberListAdapter
        lifecycleScope.launch {
            promiseSettingViewModel.promiseUiState.collect { promiseUiState ->
                promiseMemberListAdapter.submitList(promiseUiState.members)
            }
        }

        lifecycleScope.launch {
            promiseSettingViewModel.dialogEventFlow.collectLatest { event: PromiseSettingEvent ->
                when (event) {
                    PromiseSettingEvent.SELECT_DATE -> showDatePicker()
                    PromiseSettingEvent.SELECT_TIME -> showTimePicker()
                    PromiseSettingEvent.SELECT_PLACE -> showPlaceSearchDialog()
                    PromiseSettingEvent.SELECT_MEMBER -> showMember()
                }
            }
        }

        lifecycleScope.launch {
            promiseSettingViewModel.promiseSettingUiState.collectLatest { promiseSettingUiState ->
                when (promiseSettingUiState) {
                    PromiseSettingUiState.Edit -> return@collectLatest
                    PromiseSettingUiState.Success -> return@collectLatest // move to detail promise view
                    is PromiseSettingUiState.Fail -> showStateSnackbar(promiseSettingUiState.message)
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
        val promiseTitle = binding.editTextPromiseSettingPromiseTitle.text.toString()
        promiseSettingViewModel.setPromiseTitle(promiseTitle)
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
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
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
                )
            )
        }
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val isSystem24Hour = DateFormat.is24HourFormat(this)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(cal.get(Calendar.HOUR_OF_DAY))
            .setMinute(cal.get(Calendar.MINUTE))
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTitleText(getString(R.string.title_timepicker))
            .build()

        timePicker.show(supportFragmentManager, TIMEPICKER_TAG)
        timePicker.addOnPositiveButtonClickListener {
            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
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
            .setOnSelectPlaceSearchListener { searchedPlace ->
                promiseSettingViewModel.setPromiseDestination(
                    searchedPlace.title,
                    searchedPlace.location
                )
            }
            .show(supportFragmentManager, SEARCH_DIALOG_TAG)
    }

    private fun showMember() {
        val memberList = ArrayList(promiseSettingViewModel.promiseUiState.value.members)
        val intent = Intent(this, InviteActivity::class.java).putParcelableArrayListExtra(
            MEMBER_LIST_KEY,
            memberList
        )
        getContent.launch(intent)
    }

    private fun showStateSnackbar(message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

    }

    companion object {
        const val DATEPICKER_TAG = "New Selected Date"
        const val TIMEPICKER_TAG = "New Selected Time"
        const val SEARCH_DIALOG_TAG = "New Search Address Dialog"
        const val MEMBER_LIST_KEY = "memberList"
    }

}