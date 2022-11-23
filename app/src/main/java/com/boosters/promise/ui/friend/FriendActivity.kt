package com.boosters.promise.ui.friend

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityFriendBinding
import com.boosters.promise.ui.friend.adapter.UserListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        val userListAdapter = UserListAdapter()
        binding.recyclerViewFriend.adapter = userListAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                friendViewModel.friendsList.collectLatest { friendsList ->
                    userListAdapter.submitList(friendsList)
                }
            }
        }
    }

}