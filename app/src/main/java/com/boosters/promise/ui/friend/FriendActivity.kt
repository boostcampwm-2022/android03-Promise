package com.boosters.promise.ui.friend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityFriendBinding

class FriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_friend, null, false)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarFriend)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}