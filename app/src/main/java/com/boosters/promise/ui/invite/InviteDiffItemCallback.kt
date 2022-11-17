package com.boosters.promise.ui.invite

import androidx.recyclerview.widget.DiffUtil
import com.boosters.promise.ui.model.UserUiState

class InviteDiffItemCallback : DiffUtil.ItemCallback<UserUiState>() {

    override fun areItemsTheSame(oldItem: UserUiState, newItem: UserUiState): Boolean {
        return oldItem.userCode == newItem.userCode
    }

    override fun areContentsTheSame(oldItem: UserUiState, newItem: UserUiState): Boolean {
        return oldItem == newItem
    }

}