package com.boosters.promise.invite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.databinding.ItemInviteMemberInfoBinding

class InviteAdapter : ListAdapter<User, InviteAdapter.InviteViewHolder>(InviteDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteViewHolder {
        return InviteViewHolder(
            ItemInviteMemberInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InviteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class InviteViewHolder(
        private val binding: ItemInviteMemberInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.user = user
        }
    }
}