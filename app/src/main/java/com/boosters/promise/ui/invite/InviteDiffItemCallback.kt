package com.boosters.promise.ui.invite

import androidx.recyclerview.widget.DiffUtil
import com.boosters.promise.ui.model.UserState

class InviteDiffItemCallback : DiffUtil.ItemCallback<UserState>() {

    override fun areItemsTheSame(oldItem: UserState, newItem: UserState): Boolean {
        return oldItem.userCode == newItem.userCode
    }

    override fun areContentsTheSame(oldItem: UserState, newItem: UserState): Boolean {
        return oldItem == newItem
    }

}