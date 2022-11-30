package com.boosters.promise.ui.promisesetting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.R
import com.boosters.promise.databinding.ItemPromiseMemberBinding
import com.boosters.promise.ui.invite.model.UserUiModel

class PromiseMemberListAdapter(
    private val onClickListener: (item: UserUiModel) -> Unit
) : ListAdapter<UserUiModel, PromiseMemberListAdapter.PromiseMemberListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromiseMemberListViewHolder {
        val binding: ItemPromiseMemberBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_promise_member,
            parent,
            false
        )
        val promiseMemberListViewHolder = PromiseMemberListViewHolder(binding)
        binding.apply {
            root.setOnClickListener {
                onClickListener(getItem(promiseMemberListViewHolder.adapterPosition))
            }
        }
        return promiseMemberListViewHolder
    }

    override fun onBindViewHolder(holder: PromiseMemberListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PromiseMemberListViewHolder(private val binding: ItemPromiseMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserUiModel) {
            binding.userUiModel = item
        }

    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UserUiModel>() {
            override fun areItemsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
                return oldItem.userCode == newItem.userCode
            }

            override fun areContentsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
                return oldItem == newItem
            }
        }
    }

}