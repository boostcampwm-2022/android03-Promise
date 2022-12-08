package com.boosters.promise.ui.invite

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.toUserUiModel
import com.boosters.promise.databinding.ActivityInviteBinding
import com.boosters.promise.ui.invite.adapter.FriendAdapter
import com.boosters.promise.ui.invite.adapter.InviteMemberAdapter
import com.boosters.promise.ui.invite.model.UserUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteBinding
    private val inviteViewModel: InviteViewModel by viewModels()
    private val friendAdapter = FriendAdapter()
    private val inviteMemberAdapter = InviteMemberAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite)
        setBinding()
        observeCurrentFriendItems()
        observeCurrentMemberItems()
        setFriendItemClickListener()
        setMemberItemClickListener()
        setConfirmButtonClickListener()
        setQueryTextListener()

        setSupportActionBar(binding.toolbarInvite)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        if (savedInstanceState == null) {
            inviteViewModel.initAllFriendItems()
            initMemberItems()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onNavigateUp()
    }

    private fun setBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = inviteViewModel
        binding.recyclerViewInviteFriendList.adapter = friendAdapter
        binding.recyclerViewInviteMemberList.adapter = inviteMemberAdapter
    }

    private fun observeCurrentFriendItems() {
        inviteViewModel.currentFriendItems.observe(this) { friendItems ->
            friendAdapter.submitList(friendItems)
        }
    }

    private fun observeCurrentMemberItems() {
        inviteViewModel.currentMemberItems.observe(this) { memberItems ->
            inviteMemberAdapter.submitList(memberItems)
        }
    }

    private fun setFriendItemClickListener() {
        friendAdapter.setOnItemClickListener(object : FriendAdapter.OnItemClickListener {
            override fun onItemClick(user: UserUiModel, pos: Int) {
                inviteViewModel.addMemberItems(user)
            }
        })
    }

    private fun setMemberItemClickListener() {
        inviteMemberAdapter.setOnItemClickListener(
            object : InviteMemberAdapter.OnItemClickListener {
                override fun onItemClick(user: UserUiModel) {
                    inviteViewModel.removeMemberItems(user)
                }
            }
        )
    }

    private fun setConfirmButtonClickListener() {
        binding.buttonInviteConfirm.setOnClickListener {
            val intent = Intent().apply {
                val memberList = ArrayList(inviteMemberAdapter.currentList)
                putParcelableArrayListExtra(MEMBER_LIST_KEY, memberList)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun setQueryTextListener() {
        binding.searchViewInviteSearchMember.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    inviteViewModel.searchFriendItems(newText)
                }
                return true
            }
        })
    }

    private fun initMemberItems() {
        val memberItems = if (Build.VERSION.SDK_INT < 33) {
            intent.getParcelableArrayListExtra(MEMBER_LIST_KEY)
        } else {
            intent.getParcelableArrayListExtra(MEMBER_LIST_KEY, User::class.java)
        }

        inviteViewModel.initMemberItems(memberItems?.map { it.toUserUiModel() })
    }

    companion object {
        const val MEMBER_LIST_KEY = "memberList"
    }

}