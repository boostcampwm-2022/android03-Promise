package com.boosters.promise.ui.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.databinding.ItemPromiseDetailMemberBinding
import com.boosters.promise.ui.detail.model.PromiseDetailUiModel

class PromiseMemberAdapter :
    ListAdapter<PromiseDetailUiModel, PromiseMemberAdapter.PromiseMemberViewHolder>(
        diffUtil
    ) {

    private var onItemClickListener: OnItemClickListener? = null

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

    class PromiseMemberViewHolder(
        private val binding: ItemPromiseDetailMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: PromiseDetailUiModel) {
            binding.user = user

            if (user.isArrived) {
                binding.textViewPromiseMemberItemArrive.visibility = View.VISIBLE
            } else {
                binding.textViewPromiseMemberItemArrive.visibility = View.INVISIBLE
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(user: PromiseDetailUiModel, position: Int)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PromiseDetailUiModel>() {
            override fun areItemsTheSame(
                oldItem: PromiseDetailUiModel,
                newItem: PromiseDetailUiModel
            ): Boolean {
                return oldItem.userCode == newItem.userCode
            }

            override fun areContentsTheSame(
                oldItem: PromiseDetailUiModel,
                newItem: PromiseDetailUiModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}