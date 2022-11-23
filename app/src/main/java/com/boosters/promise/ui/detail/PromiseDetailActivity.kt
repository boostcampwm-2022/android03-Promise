package com.boosters.promise.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityPromiseDetailBinding
import com.boosters.promise.ui.promisesetting.model.PromiseUiState

class PromiseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromiseDetailBinding
    private val promiseDetailViewModel: PromiseDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_promise_detail)
        setBinding()
        setPromiseInfo()
    }

    private fun setBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = promiseDetailViewModel
    }

    private fun setPromiseInfo() {
        val promise = if (Build.VERSION.SDK_INT < 33) {
            intent.getParcelableExtra(PROMISE_INFO_KEY)
        } else {
            intent.getParcelableExtra(PROMISE_INFO_KEY, PromiseUiState::class.java)
        }

        if (promise != null) {
            promiseDetailViewModel.setPromiseInfo(promise)
        }
    }

    companion object {
        const val PROMISE_INFO_KEY = "promise"
    }

}