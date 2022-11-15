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

    private var binding: ActivityInviteBinding? = null
    private val inviteViewModel: InviteViewModel by viewModels()
    private val inviteAdapter = InviteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite)
        checkNotNull(binding).lifecycleOwner = this
        checkNotNull(binding).viewModel = inviteViewModel
        checkNotNull(binding).recyclerViewMemberFriendList.adapter = inviteAdapter
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}