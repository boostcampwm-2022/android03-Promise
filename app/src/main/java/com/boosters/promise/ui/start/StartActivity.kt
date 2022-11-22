package com.boosters.promise.ui.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.boosters.promise.ui.promisecalendar.PromiseCalendarActivity
import com.boosters.promise.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    private val startViewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                initObserver()
                true
            }
        }
        super.onCreate(savedInstanceState)
    }

    private fun initObserver() {
        if (startViewModel.isSignUp.hasActiveObservers().not()) {
            startViewModel.isSignUp.observe(this@StartActivity) { isSignUp ->
                val intent = Intent(
                    this@StartActivity,
                    if (isSignUp) PromiseCalendarActivity::class.java else SignUpActivity::class.java
                )
                startActivity(intent).also { finish() }
            }
        }
    }

}