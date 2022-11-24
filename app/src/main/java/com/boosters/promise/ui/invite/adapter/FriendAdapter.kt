package com.boosters.promise.ui.invite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.R
import com.boosters.promise.databinding.ItemInviteFriendBinding
import com.boosters.promise.ui.invite.model.UserUiModel

class FriendAdapter : ListAdapter<UserUiModel, FriendAdapter.FriendViewHolder>(
    UserUiModelDiffItemCallback
) {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInviteFriendBinding.inflate(inflater, parent, false)
        val holder = FriendViewHolder(binding)

        binding.root.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(getItem(position), position)
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

        fun bind(userUiModel: UserUiModel) {
            binding.user = userUiModel

            if (userUiModel.isSelected) {
                binding.textViewInviteUserName.setTextAppearance(R.style.Promise_TextAppearance_MiddleGray)
                binding.textViewInviteUserCode.setTextAppearance(R.style.Promise_TextAppearance_MiddleGray)
            } else {
                binding.textViewInviteUserName.setTextAppearance(R.style.Promise_TextAppearance_MiddleBold)
                binding.textViewInviteUserCode.setTextAppearance(R.style.Promise_TextAppearance_MiddleNormal)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: UserUiModel, pos: Int)
    }

}