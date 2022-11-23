package com.boosters.promise.ui.promisesetting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityPromiseDetailBinding
import com.boosters.promise.ui.promisecalendar.PromiseCalendarActivity
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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        promiseDetailViewModel.isDeleted.observe(this) {
            if (it) {
                startActivity(
                    Intent(this, PromiseCalendarActivity::class.java)
                ).also { finish() }
                finish()
            } else {
                showStateSnackbar(R.string.promiseDetail_Delete_Fail)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val intent = Intent(this, PromiseSettingActivity::class.java).putExtra(
                    PROMISE_KEY,
                    promiseDetailViewModel.promiseUiState.value
                )
                startActivity(intent)
                finish()
            }
            R.id.delete -> {
                deletePromise()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showStateSnackbar(message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun deletePromise() {
        AlertDialog.Builder(this)
            .setTitle("삭제")
            .setMessage("약속 정보를 삭제합니다.")
            .setPositiveButton("YES") { dialog , which ->
                promiseDetailViewModel.removePromise()
                startActivity(
                    Intent(this, PromiseCalendarActivity::class.java)
                ).also { finish() }
            }
            .setNegativeButton("NO"){ dialog, which ->
                return@setNegativeButton
            }
            .create()
            .show()
    }

    companion object {
        private const val PROMISE_KEY = "promise"
    }

}