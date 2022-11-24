package com.boosters.promise.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.databinding.ItemPromiseDetailMemberBinding
import com.boosters.promise.ui.invite.adapter.UserDiffItemCallback
import com.boosters.promise.ui.invite.model.UserUiState

class PromiseMemberAdapter : ListAdapter<UserUiState, PromiseMemberAdapter.PromiseMemberViewHolder>(
    UserDiffItemCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromiseMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPromiseDetailMemberBinding.inflate(inflater, parent, false)

        return PromiseMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromiseMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PromiseMemberViewHolder(
        private val binding: ItemPromiseDetailMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(userUiState: UserUiState) {
            binding.user = userUiState
        }

    }

}