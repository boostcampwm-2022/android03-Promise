package com.boosters.promise.ui.friend

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.data.user.User
import com.boosters.promise.databinding.ActivityFriendBinding
import com.boosters.promise.ui.friend.adapter.UserListAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
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
                }
            })
        }
        binding.recyclerViewFriend.adapter = userListAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    friendViewModel.usersList.collectLatest { usersList ->
                        userListAdapter.submitList(usersList)

                        binding.isEmpty = usersList.isEmpty()
                    }
                }
            }
        }

        lifecycleScope.launch {
            friendViewModel.selectedTab.first().let {
                binding.tabFriend.run { selectTab(getTabAt(it)) }
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
                        friendViewModel.searchUser("")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

        binding.searchViewFriend.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                when (binding.tabFriend.selectedTabPosition) {
                    FRIEND_TAB_INDEX -> friendViewModel.searchFriend(query.orEmpty())
                    ALLUSER_TAB_INDEX -> friendViewModel.searchUser(query.orEmpty())
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean = false
        })

        lifecycleScope.launch {
            friendViewModel.networkConnection.collectLatest {
                if (!it) {
                    Snackbar.make(binding.root, R.string.signUp_networkError, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    companion object {
        const val FRIEND_TAB_INDEX = 0
        const val ALLUSER_TAB_INDEX = 1
    }

}