package com.boosters.promise.ui.invite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityInviteBinding
import com.boosters.promise.ui.invite.adapter.InviteAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteBinding
    private val inviteViewModel: InviteViewModel by viewModels()
    private val inviteAdapter = InviteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite)
        setBinding()
        observeAllFriendItems()
        observeCurrentFriendItems()

        inviteViewModel.loadAllFriendItems()
    }

    private fun setBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = inviteViewModel
        binding.recyclerViewInviteFriendList.adapter = inviteAdapter
    }

    private fun observeAllFriendItems() {
        inviteViewModel.allFriendItems.observe(this) { friendItems ->
            inviteViewModel.setCurrentFriendItems(friendItems)
        }
    }

    private fun observeCurrentFriendItems() {
        inviteViewModel.currentFriendItems.observe(this) { friendItems ->
            inviteAdapter.submitList(friendItems)
        }
    }

}