package com.boosters.promise.ui.promisecalendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityPromiseCalendarBinding

class PromiseCalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromiseCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_promise_calendar, null, false)
        binding.lifecycleOwner = this
        setContentView(binding.root)

    }
}