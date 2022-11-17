package com.boosters.promise.ui.invite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityInviteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteBinding
    private val inviteViewModel: InviteViewModel by viewModels()
    private val inviteAdapter = InviteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite)
        binding.lifecycleOwner = this
        binding.viewModel = inviteViewModel
        binding.recyclerViewInviteFriendList.adapter = inviteAdapter
    }

}