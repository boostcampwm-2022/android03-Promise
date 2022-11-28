package com.boosters.promise.ui.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.data.user.User
import com.boosters.promise.databinding.ItemPromiseDetailMemberBinding

class PromiseMemberAdapter : ListAdapter<User, PromiseMemberAdapter.PromiseMemberViewHolder>(
    diffUtil
) {

    private var onItemClickListener: OnItemClickListener? = null
    var arrivedMember = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromiseMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPromiseDetailMemberBinding.inflate(inflater, parent, false)
        val holder = PromiseMemberViewHolder(binding)

        binding.root.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(getItem(position), position)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: PromiseMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class PromiseMemberViewHolder(
        private val binding: ItemPromiseDetailMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.user = user

            if (adapterPosition == arrivedMember) {
                binding.textViewPromiseMemberItemArrive.visibility = View.VISIBLE
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(user: User, position: Int)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem.userCode == newItem.userCode
            }

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}