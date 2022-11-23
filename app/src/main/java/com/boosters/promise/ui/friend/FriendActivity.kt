package com.boosters.promise.ui.friend

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.data.user.User
import com.boosters.promise.databinding.ActivityFriendBinding
import com.boosters.promise.ui.friend.adapter.UserListAdapter
import com.google.android.material.tabs.TabLayout
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

        val userListAdapter = UserListAdapter().apply {
            setOnAddButtonClickListener(object : UserListAdapter.OnClickListener {
                override fun onClick(user: User) {
                    friendViewModel.addFriend(user)
                    friendViewModel.loadAllUsersList()
                }
            })
        }
        binding.recyclerViewFriend.adapter = userListAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    friendViewModel.usersList.collectLatest { usersList ->
                        userListAdapter.submitList(usersList)
                    }
                }
            }
        }

        binding.tabFriend.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    FRIEND_TAB_INDEX -> {
                        userListAdapter.setAddButtonVisible(false)
                        friendViewModel.loadFriendsList()
                    }
                    ALLUSER_TAB_INDEX -> {
                        userListAdapter.setAddButtonVisible(true)
                        friendViewModel.loadAllUsersList()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    companion object {
        const val FRIEND_TAB_INDEX = 0
        const val ALLUSER_TAB_INDEX = 1
    }

}