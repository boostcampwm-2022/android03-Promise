package com.boosters.promise.ui.promisecalendar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.activity.viewModels
import androidx.appcompat.R.attr.colorPrimary
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.databinding.ActivityPromiseCalendarBinding
import com.boosters.promise.ui.detail.PromiseDetailActivity
import com.boosters.promise.ui.friend.FriendActivity
import com.boosters.promise.ui.promisecalendar.adapter.PromiseDailyListAdapter
import com.boosters.promise.ui.promisecalendar.decorator.PromiseContainDecorator
import com.boosters.promise.ui.promisecalendar.decorator.PromiseSaturdayDecorator
import com.boosters.promise.ui.promisecalendar.decorator.PromiseSundayDecorator
import com.boosters.promise.ui.promisecalendar.decorator.PromiseTodayDecorator
import com.boosters.promise.ui.promisecalendar.model.PromiseListUiState
import com.boosters.promise.ui.promisesetting.PromiseSettingActivity
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class PromiseCalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromiseCalendarBinding
    private val promiseCalendarViewModel: PromiseCalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()

        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.activity_promise_calendar, null, false)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        attachAdapter()
        bindCalendarView()

        binding.imageViewPromiseCalendarFriendsList.setOnClickListener {
            startActivity(Intent(this, FriendActivity::class.java))
        }

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
                    Snackbar.make(
                        binding.root,
                        R.string.start_item_notification_permission,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
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

    private fun attachAdapter() {
        val promiseDailyListAdapter = PromiseDailyListAdapter()
        binding.recyclerViewPromiseCalendarDailyList.adapter = promiseDailyListAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                promiseCalendarViewModel.dailyPromiseList.collectLatest { promiseList ->
                    if (promiseList?.isEmpty() == true) {
                        promiseDailyListAdapter.submitList(promiseList + listOf(Promise()))
                    } else {
                        promiseDailyListAdapter.submitList(promiseList)
                    }
                }
            }
        }

        promiseDailyListAdapter.setOnItemClickListener(object :
            PromiseDailyListAdapter.OnItemClickListener {
            override fun onItemClick(promise: Promise) {
                val intent = Intent(this@PromiseCalendarActivity, PromiseDetailActivity::class.java)
                intent.putExtra(PROMISE_ID_KEY, promise.promiseId)
                startActivity(intent)
            }
        })
    }

    private fun bindCalendarView() {
        val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val primaryColor = TypedValue().also {
            theme.resolveAttribute(colorPrimary, it, true)
        }.data

        val promiseDayList = arrayListOf<CalendarDay>()

        val promiseContainDecorator = PromiseContainDecorator(promiseDayList, primaryColor)
        val promiseSaturdayDecorator = PromiseSaturdayDecorator()
        val promiseSundayDecorator = PromiseSundayDecorator()
        val promiseTodayCalendarDecorator = PromiseTodayDecorator(
            ContextCompat.getDrawable(
                this,
                R.drawable.bg_calendar_today_circle
            )
        )
        binding.materialCalendarViewPromiseCalendar.addDecorators(
            promiseTodayCalendarDecorator,
            promiseSaturdayDecorator,
            promiseSundayDecorator
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                promiseCalendarViewModel.myPromiseList.collectLatest {
                    if (it is PromiseListUiState.Success) {
                        promiseDayList.clear()
                        promiseDayList.addAll(
                            it.data.map { promise ->
                                CalendarDay.from(
                                    Calendar.getInstance().apply {
                                        time = dateFormatter.parse(promise.date)
                                    }
                                )
                            }
                        )

                        binding.materialCalendarViewPromiseCalendar.removeDecorator(
                            promiseContainDecorator
                        )
                        binding.materialCalendarViewPromiseCalendar.addDecorator(
                            promiseContainDecorator
                        )
                    }
                }
            }
        }

        with(CalendarDay.today()) {
            val today = getString(R.string.date_format).format(year, month + 1, day)
            promiseCalendarViewModel.updateDailyPromiseList(today)
            binding.materialCalendarViewPromiseCalendar.selectedDate = this
        }

        binding.materialCalendarViewPromiseCalendar.setOnDateChangedListener { _, date, _ ->
            val selectedDate = with(date) {
                getString(R.string.date_format).format(year, month + 1, day)
            }
            promiseCalendarViewModel.updateDailyPromiseList(selectedDate)
        }

        lifecycleScope.launch {
            promiseCalendarViewModel.networkConnection.collectLatest {
                if (!it) {
                    Snackbar.make(binding.root, R.string.signUp_networkError, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    companion object {
        const val PROMISE_ID_KEY = "promiseId"
        const val DATE_FORMAT = "yyyy/MM/dd"
    }

}