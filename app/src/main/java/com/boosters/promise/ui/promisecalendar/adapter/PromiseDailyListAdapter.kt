package com.boosters.promise.ui.promisecalendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.R
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.databinding.ItemPromiseCardBinding

class PromiseDailyListAdapter :
    ListAdapter<Promise, PromiseDailyListAdapter.PlaceSearchViewHolder>(diffUtil) {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceSearchViewHolder {
        val binding: ItemPromiseCardBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_promise_card,
            parent,
            false
        )
        val holder = PlaceSearchViewHolder(binding)

        binding.root.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(getItem(position))
            }
        }

        return holder
    }

    override fun onBindViewHolder(viewHolder: PlaceSearchViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class PlaceSearchViewHolder(private val binding: ItemPromiseCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Promise) {
            binding.promise = item
            binding.executePendingBindings()
        }

    }

    interface OnItemClickListener {
        fun onItemClick(promise: Promise)
    }

    companion object {
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