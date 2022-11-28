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
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.databinding.ActivityPromiseCalendarBinding
import com.boosters.promise.ui.detail.PromiseDetailActivity
import com.boosters.promise.ui.friend.FriendActivity
import com.boosters.promise.ui.promisecalendar.adapter.PromiseDailyListAdapter
import com.boosters.promise.ui.promisecalendar.model.PromiseListUiState
import com.boosters.promise.ui.promisesetting.PromiseSettingActivity
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        val promiseDailyListAdapter = PromiseDailyListAdapter()
        binding.recyclerViewPromiseCalendarDailyList.adapter = promiseDailyListAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                promiseCalendarViewModel.myPromiseList.collectLatest {
                    if (it is PromiseListUiState.Success) {
                        promiseDailyListAdapter.submitList(it.data)
                    }
                }
            }
        }

        binding.materialCalendarViewPromiseCalendar.selectedDate = CalendarDay.today()

        binding.imageViewPromiseCalendarFriendsList.setOnClickListener {
            startActivity(Intent(this, FriendActivity::class.java))
        }

        binding.buttonPromiseCalendarCreatePromise.setOnClickListener {
            startActivity(Intent(this, PromiseSettingActivity::class.java))
        }

        promiseDailyListAdapter.setOnItemClickListener(object : PromiseDailyListAdapter.OnItemClickListener {
            override fun onItemClick(promise: Promise) {
                val intent = Intent(this@PromiseCalendarActivity, PromiseDetailActivity::class.java)
                intent.putExtra(PROMISE_ID_KEY, promise.promiseId)
                startActivity(intent)
            }
        })
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

    companion object {
        const val PROMISE_ID_KEY = "promiseId"
    }

}