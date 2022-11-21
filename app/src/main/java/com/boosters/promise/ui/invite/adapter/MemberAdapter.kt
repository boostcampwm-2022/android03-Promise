package com.boosters.promise.ui.invite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.databinding.ItemInviteMemberBinding
import com.boosters.promise.ui.invite.model.UserUiState

class MemberAdapter : ListAdapter<UserUiState, MemberAdapter.MemberViewHolder>(
    UserDiffItemCallback()
) {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInviteMemberBinding.inflate(inflater, parent, false)
        val holder = MemberViewHolder(binding)

        binding.root.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(getItem(position))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class MemberViewHolder(
        private val binding: ItemInviteMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userUiState: UserUiState) {
            binding.user = userUiState
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: UserUiState)
    }

}
