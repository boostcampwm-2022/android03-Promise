package com.boosters.promise.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.databinding.ItemPromiseDetailMemberBinding
import com.boosters.promise.ui.detail.model.MemberUiModel

class PromiseMemberAdapter :
    ListAdapter<MemberUiModel, PromiseMemberAdapter.PromiseMemberViewHolder>(
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
                onItemClickListener?.onItemClick(position)
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

        fun bind(memberUiModel: MemberUiModel) {
            binding.memberUiModel = memberUiModel
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MemberUiModel>() {
            override fun areItemsTheSame(
                oldItem: MemberUiModel,
                newItem: MemberUiModel
            ): Boolean {
                return oldItem.userCode == newItem.userCode
            }

            override fun areContentsTheSame(
                oldItem: MemberUiModel,
                newItem: MemberUiModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}