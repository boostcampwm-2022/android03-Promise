package com.boosters.promise.ui.promise

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.inflate
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.boosters.promise.R
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.databinding.ActivityPromiseDetailBinding
import com.boosters.promise.databinding.ActivityPromiseSettingBinding
import com.boosters.promise.ui.invite.model.UserUiState
import com.boosters.promise.ui.promise.model.PromiseUiState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromiseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromiseDetailBinding
    private val promiseDetailViewModel: PromiseDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        promiseDetailViewModel.isDeleted.observe(this) {
            if (it) {
                // move to calendar view
                finish()
            } else {
                showStateSnackbar(R.string.promiseDetail_Delete_Fail)
            }
        }

        binding.buttonPromiseDeailDelete.setOnClickListener {
            promiseDetailViewModel.removePromise()
        }

        binding.buttonPromiseDetailEdit.setOnClickListener {
            val intent = Intent(this, PromiseSettingActivity::class.java).putExtra(
                PROMISE_KEY,
                promiseDetailViewModel.promiseUiState.value
            )
            startActivity(intent)
            finish()
        }
    }

    private fun showStateSnackbar(message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val PROMISE_KEY = "promise"
    }

}