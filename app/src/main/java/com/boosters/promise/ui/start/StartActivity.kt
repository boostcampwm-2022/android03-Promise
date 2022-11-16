package com.boosters.promise.ui.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.boosters.promise.databinding.ActivityStartBinding
import com.boosters.promise.ui.start.adapter.PromiseListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private val viewModel: StartViewModel by viewModels()
    private lateinit var promiseListAdapter: PromiseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                val start = System.currentTimeMillis()
                // Job
                val end = System.currentTimeMillis()
                Thread.sleep((SPLASH_SCREEN_DURATION - (end - start)).coerceAtLeast(0))
                false
            }
        }
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObserver()

        promiseListAdapter = PromiseListAdapter {}
        binding.recyclerViewHomePromiseList.adapter = promiseListAdapter

        binding.buttonHomeGetPromise.setOnClickListener {
            viewModel.getPromiseList(binding.editTextHomeGetPromise.text.toString())
        }
    }

    private fun setObserver() {
        viewModel.promiseList.observe(this) {
            promiseListAdapter.submitList(it)
        }
    }

    companion object {
        private const val SPLASH_SCREEN_DURATION = 1_000
    }

}