package com.boosters.promise.ui.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.boosters.promise.R

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                false
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

}