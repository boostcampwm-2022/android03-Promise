package com.boosters.promise.ui.promisecalendar

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityPromiseCalendarBinding
import com.boosters.promise.ui.place.PlaceSearchViewModel
import com.boosters.promise.ui.promisecalendar.adapter.PromiseDailyListAdapter
import com.boosters.promise.ui.promisesetting.PromiseSettingActivity
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

}