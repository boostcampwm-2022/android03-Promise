package com.boosters.promise.ui.notification

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootService : LifecycleService() {

    @Inject
    lateinit var promiseRepository: PromiseRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var alarmDirector: AlarmDirector


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycleScope.launch {
            alarmDirector.removeLocalAllAlarm()
            userRepository.getMyInfo().first().onSuccess { myInfo ->
                promiseRepository.getPromiseList(myInfo).first().forEach { promise ->
                    alarmDirector.registerAlarm(promise)
                }
            }
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

}