package com.boosters.promise.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
@BindingAdapter("bind_items")
fun <T> RecyclerView.setItems(items: List<T>?) {
    (adapter as ListAdapter<T, RecyclerView.ViewHolder>).submitList(items) {
        scrollToPosition(0)
    }
}