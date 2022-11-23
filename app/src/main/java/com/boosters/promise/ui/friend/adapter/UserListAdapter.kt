package com.boosters.promise.ui.friend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.R
import com.boosters.promise.data.user.User
import com.boosters.promise.databinding.ItemFriendProfileBinding

class UserListAdapter : ListAdapter<User, UserListAdapter.FriendViewHolder>(diffUtil) {

    private var onItemClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding: ItemFriendProfileBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_friend_profile,
            parent,
            false
        )
        val friendViewHolder = FriendViewHolder(binding)
        if (onItemClickListener != null) {
            binding.buttonFriendAdd.setOnClickListener {
                checkNotNull(onItemClickListener).onClick(getItem(friendViewHolder.adapterPosition))
            }
        }

        return friendViewHolder
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnAddButtonClickListener(listener: OnClickListener) {
        onItemClickListener = listener
    }

    class FriendViewHolder(
        private val binding: ItemFriendProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.user = user
            binding.executePendingBindings()
        }
    }

    interface OnClickListener {
        fun onClick(user: User)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.userCode == newItem.userCode
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

}