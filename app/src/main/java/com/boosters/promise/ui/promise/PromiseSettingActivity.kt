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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityPromiseSettingBinding
import com.boosters.promise.ui.invite.InviteActivity
import com.boosters.promise.ui.invite.model.UserUiState
import com.boosters.promise.ui.place.PlaceSearchDialogFragment
import com.boosters.promise.ui.promise.adapter.PromiseMemberListAdapter
import com.boosters.promise.ui.promise.model.PromiseSettingEvent
import com.boosters.promise.ui.promise.model.PromiseSettingUiState
import com.boosters.promise.ui.promise.model.PromiseUiState
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseSettingBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.promiseSettingViewModel = promiseSettingViewModel
        setContentView(binding.root)

        //checkPermissions()
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

        initPromise()

    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (binding.editTextPromiseSettingPromiseTitle.hasFocus()) {
            currentFocus?.clearFocus()
            hideKeyBoard()
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showStateSnackbar(R.string.start_item_notification_permission)
                }
            }
        }
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

    private fun checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), PERMISSIONS_REQUEST)
            }
        }
    }

    private fun initPromise() {
        if (Build.VERSION.SDK_INT < 33) {
            intent.getParcelableExtra(PROMISE_KEY)
        } else {
            intent.getParcelableExtra(PROMISE_KEY, PromiseUiState::class.java)
        }?.let {
            promiseSettingViewModel.initPromise(it)
        }
    }

    companion object {
        const val DATEPICKER_TAG = "New Selected Date"
        const val TIMEPICKER_TAG = "New Selected Time"
        const val SEARCH_DIALOG_TAG = "New Search Address Dialog"
        const val MEMBER_LIST_KEY = "memberList"
        private const val PROMISE_KEY = "promise"
        const val PERMISSIONS_REQUEST = 0x0000001
    }

}