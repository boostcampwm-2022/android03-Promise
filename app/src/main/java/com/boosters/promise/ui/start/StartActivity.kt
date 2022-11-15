package com.boosters.promise.ui.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.boosters.promise.R

class StartActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_start)

    }

    companion object {
        private const val SPLASH_SCREEN_DURATION = 1_000
    }

}