package com.boosters.promise.ui.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.boosters.promise.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    private val startViewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                startViewModel.isSignUp.observe(this@StartActivity) { isSignUp ->
                    if (isSignUp) {
//                        startActivity(Intent(this@StartActivity, PromiseSettingActivity::class.java))
                        // TODO: Home 약속 리스트 화면으로 이동 구현
                    } else {
                        startActivity(Intent(this@StartActivity, SignUpActivity::class.java))
                    }
                    finish()
                }
                true
            }
        }
        super.onCreate(savedInstanceState)
    }

}