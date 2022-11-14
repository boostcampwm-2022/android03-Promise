package com.boosters.promise.invite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityInviteBinding

class InviteActivity : AppCompatActivity() {
    private var binding: ActivityInviteBinding? = null
    private val viewModel: InviteViewModel = InviteViewModel(FriendFakeRepository())
    private val inviteAdapter = InviteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite)
        checkNotNull(binding).lifecycleOwner = this
        checkNotNull(binding).viewModel = viewModel
        checkNotNull(binding).recyclerViewMemberFriendList.adapter = inviteAdapter
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}