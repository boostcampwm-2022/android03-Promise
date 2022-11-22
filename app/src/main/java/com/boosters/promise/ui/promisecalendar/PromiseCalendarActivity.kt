package com.boosters.promise.ui.promisecalendar

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityPromiseCalendarBinding
import com.boosters.promise.ui.promisecalendar.adapter.PromiseDailyListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromiseCalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromiseCalendarBinding
    private val promiseCalendarViewModel: PromiseCalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding.materialCalendarViewPromiseCalendar.setOnDateChangedListener { widget, date, selected ->
            val selectedDate = date.run {
                getString(R.string.date_format).format(year, month + 1, day)
            }
            promiseCalendarViewModel.getPromiseList(selectedDate)
        }
    }
}