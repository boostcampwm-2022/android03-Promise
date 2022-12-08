package com.boosters.promise.ui.promisecalendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.boosters.promise.R
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.databinding.ItemPromiseCardBinding
import com.boosters.promise.databinding.ItemPromiseEmptyBinding

class PromiseDailyListAdapter :
    ListAdapter<Promise, ViewHolder>(diffUtil) {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            EMPTY_TYPE -> {
                val binding: ItemPromiseEmptyBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_promise_empty,
                    parent,
                    false
                )
                EmptyViewHolder(binding)
            }
            else -> {
                val binding: ItemPromiseCardBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_promise_card,
                    parent,
                    false
                )
                val holder = PromiseViewHolder(binding)

                binding.root.setOnClickListener {
                    val position = holder.adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener?.onItemClick(getItem(position))
                    }
                }
                holder
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).promiseId.isEmpty()) EMPTY_TYPE else PROMISE_DAILY_TYPE
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (getItem(position).promiseId.isNotEmpty()) {
            (viewHolder as PromiseViewHolder).bind(getItem(position))
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class PromiseViewHolder(private val binding: ItemPromiseCardBinding) :
        ViewHolder(binding.root) {

        fun bind(item: Promise) {
            binding.promise = item
            binding.executePendingBindings()
        }

    }

    class EmptyViewHolder(val binding: ItemPromiseEmptyBinding) : ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClick(promise: Promise)
    }

    companion object {
        private const val EMPTY_TYPE = 0
        private const val PROMISE_DAILY_TYPE = 1

        val diffUtil = object : DiffUtil.ItemCallback<Promise>() {
            override fun areItemsTheSame(
                oldItem: Promise,
                newItem: Promise
            ): Boolean {
                return oldItem.promiseId == newItem.promiseId
            }

            override fun areContentsTheSame(
                oldItem: Promise,
                newItem: Promise
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}