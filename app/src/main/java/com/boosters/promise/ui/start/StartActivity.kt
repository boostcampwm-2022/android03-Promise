package com.boosters.promise.ui.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.boosters.promise.ui.promise.PromiseSettingActivity
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
                // TODO: Home 약속 리스트 화면으로 이동 구현
                val intent = Intent(
                    this@StartActivity,
                    if (isSignUp) PromiseSettingActivity::class.java else SignUpActivity::class.java
                )
                startActivity(intent).also { finish() }
            }
        }
    }

}