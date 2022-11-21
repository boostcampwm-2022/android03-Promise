package com.boosters.promise.ui.invite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.databinding.ItemInviteFriendBinding
import com.boosters.promise.ui.invite.model.UserUiState

class FriendAdapter : ListAdapter<UserUiState, FriendAdapter.FriendViewHolder>(
    UserDiffItemCallback()
) {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInviteFriendBinding.inflate(inflater, parent, false)
        val holder = FriendViewHolder(binding)

        binding.root.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(getItem(position))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class FriendViewHolder(
        private val binding: ItemInviteFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userUiState: UserUiState) {
            binding.user = userUiState
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: UserUiState)
    }

}