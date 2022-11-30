package com.boosters.promise.ui.invite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.databinding.ItemInviteMemberBinding
import com.boosters.promise.ui.invite.model.UserUiModel

class InviteMemberAdapter : ListAdapter<UserUiModel, InviteMemberAdapter.InviteMemberViewHolder>(
    UserUiModelDiffItemCallback
) {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInviteMemberBinding.inflate(inflater, parent, false)
        val holder = InviteMemberViewHolder(binding)

        binding.root.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(getItem(position))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: InviteMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class InviteMemberViewHolder(
        private val binding: ItemInviteMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(userUiModel: UserUiModel) {
            binding.userUiModel = userUiModel
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: UserUiModel)
    }

}