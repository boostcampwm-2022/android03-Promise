package com.boosters.promise.ui.invite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivityInviteBinding
import com.boosters.promise.ui.invite.adapter.FriendAdapter
import com.boosters.promise.ui.invite.adapter.MemberAdapter
import com.boosters.promise.ui.invite.model.UserUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteBinding
    private val inviteViewModel: InviteViewModel by viewModels()
    private val friendAdapter = FriendAdapter()
    private val memberAdapter = MemberAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite)
        setBinding()
        observeCurrentFriendItems()
        setFriendItemClickListener()
        setMemberItemClickListener()
        setConfirmButtonClickListener()

        inviteViewModel.initAllFriendItems()
    }

    private fun setBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = inviteViewModel
        binding.recyclerViewInviteFriendList.adapter = friendAdapter
        binding.recyclerViewInviteMemberList.adapter = memberAdapter
    }

    private fun observeCurrentFriendItems() {
        inviteViewModel.currentFriendItems.observe(this) { friendItems ->
            friendAdapter.submitList(friendItems)
        }
    }

    private fun setFriendItemClickListener() {
        friendAdapter.setOnItemClickListener(object : FriendAdapter.OnItemClickListener {
            override fun onItemClick(user: UserUiState) {
                if (user !in memberAdapter.currentList) {
                    memberAdapter.submitList(memberAdapter.currentList.plus(user))
                }
            }
        })
    }

    private fun setMemberItemClickListener() {
        memberAdapter.setOnItemClickListener(object : MemberAdapter.OnItemClickListener {
            override fun onItemClick(user: UserUiState) {
                memberAdapter.submitList(memberAdapter.currentList.minus(user))
            }
        })
    }

    private fun setConfirmButtonClickListener() {
        binding.buttonInviteConfirm.setOnClickListener {
            val intent = Intent().apply {
                val memberList = ArrayList(memberAdapter.currentList)
                putParcelableArrayListExtra("memberList", memberList)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}