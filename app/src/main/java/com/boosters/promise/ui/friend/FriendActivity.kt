package com.boosters.promise.ui.friend

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityFriendBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendBinding
    private val friendViewModel: FriendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_friend, null, false)
        binding.lifecycleOwner = this
        binding.friendViewModel = friendViewModel
        binding.myInfo = friendViewModel.myInfo
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarFriend)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}