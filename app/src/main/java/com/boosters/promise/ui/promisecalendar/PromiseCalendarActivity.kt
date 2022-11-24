package com.boosters.promise.ui.promisecalendar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityPromiseCalendarBinding
import com.boosters.promise.ui.place.PlaceSearchViewModel
import com.boosters.promise.ui.promisecalendar.adapter.PromiseDailyListAdapter
import com.boosters.promise.ui.promisesetting.PromiseSettingActivity
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromiseCalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromiseCalendarBinding
    private val promiseCalendarViewModel: PromiseCalendarViewModel by viewModels()

    @OptIn(FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()

        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.activity_promise_calendar, null, false)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        val promiseDailyListAdapter = PromiseDailyListAdapter()
        binding.recyclerViewPromiseCalendarDailyList.adapter = promiseDailyListAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                promiseCalendarViewModel.promiseDailyList.collect {
                    promiseDailyListAdapter.submitList(it)
                }
            }
        }

        binding.materialCalendarViewPromiseCalendar.selectedDate = CalendarDay.today()
        binding.materialCalendarViewPromiseCalendar.dateChangesToFlow()
            .filterNot { it.isNullOrEmpty() }
            .debounce(PlaceSearchViewModel.SEARCH_TERM)
            .distinctUntilChanged()
            .onEach { date ->
                promiseCalendarViewModel.updatePromiseList(checkNotNull(date))
            }
            .launchIn(lifecycleScope)

        binding.buttonPromiseCalendarCreatePromise.setOnClickListener {
            startActivity(Intent(this, PromiseSettingActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PromiseSettingActivity.PERMISSIONS_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(binding.root, R.string.start_item_notification_permission, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun MaterialCalendarView.dateChangesToFlow(): Flow<String?> {
        return callbackFlow {
            setOnDateChangedListener { _, date, _ ->
                trySend(
                    with(date) {
                        getString(R.string.date_format).format(year, month + 1, day)
                    }
                )
            }
            awaitClose { setOnDateChangedListener(null) }
        }.onStart {
            emit(
                with(CalendarDay.today()) {
                    getString(R.string.date_format).format(year, month + 1, day)
                }
            )
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PromiseSettingActivity.PERMISSIONS_REQUEST
                )
            }
        }
    }

}