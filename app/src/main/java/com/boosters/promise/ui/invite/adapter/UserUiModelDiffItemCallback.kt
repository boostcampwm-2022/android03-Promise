package com.boosters.promise.ui.invite.adapter

import androidx.recyclerview.widget.DiffUtil
import com.boosters.promise.ui.invite.model.UserUiModel

object UserUiModelDiffItemCallback : DiffUtil.ItemCallback<UserUiModel>() {

    override fun areItemsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
        return oldItem.userCode == newItem.userCode
    }

    override fun areContentsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
        return oldItem == newItem
    }

}