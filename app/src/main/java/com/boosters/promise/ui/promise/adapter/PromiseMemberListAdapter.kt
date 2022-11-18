package com.boosters.promise.ui.promise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.R
import com.boosters.promise.databinding.ItemPromiseMemberBinding
import com.boosters.promise.ui.invite.model.UserUiState

class PromiseMemberListAdapter(
    private val onClickListener: (item: UserUiState) -> Unit
) : ListAdapter<UserUiState, PromiseMemberListAdapter.PromiseMemberListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromiseMemberListViewHolder {
        return PromiseMemberListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PromiseMemberListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PromiseMemberListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_promise_member, parent, false)
    ) {

        private val binding = ItemPromiseMemberBinding.bind(itemView)

        fun bind(item: UserUiState) {
            binding.userUiState = item
            itemView.setOnClickListener {
                onClickListener(item)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UserUiState>() {
            override fun areItemsTheSame(oldItem: UserUiState, newItem: UserUiState): Boolean {
                return oldItem.userCode == newItem.userCode
            }

            override fun areContentsTheSame(oldItem: UserUiState, newItem: UserUiState): Boolean {
                return oldItem == newItem
            }
        }
    }

}