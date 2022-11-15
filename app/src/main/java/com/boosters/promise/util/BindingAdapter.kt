package com.boosters.promise.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
@BindingAdapter("items")
fun <T> RecyclerView.setItems(items: List<T>?) {

    if (items.isNullOrEmpty()) {
        return
    }

    (adapter as ListAdapter<T, RecyclerView.ViewHolder>).submitList(items) {
        scrollToPosition(0)
    }

}