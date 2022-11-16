package com.boosters.promise.ui.start.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.databinding.ItemPromiseBinding
import com.boosters.promise.ui.model.Promise

class PromiseListAdapter(
    private val onClickListener: (item: Promise) -> Unit
) : ListAdapter<Promise, PromiseListAdapter.PromiseListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromiseListViewHolder {
        return PromiseListViewHolder(
            ItemPromiseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PromiseListViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    inner class PromiseListViewHolder(private val binding: ItemPromiseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Promise) {
            binding.promise = item
            itemView.setOnClickListener {
                // 상세화면 이동 로직 추가 예정
            }
        }

    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Promise>() {
            override fun areItemsTheSame(oldItem: Promise, newItem: Promise): Boolean {
                return oldItem.promiseId == newItem.promiseId
            }

            override fun areContentsTheSame(oldItem: Promise, newItem: Promise): Boolean {
                return oldItem == newItem
            }
        }
    }

}