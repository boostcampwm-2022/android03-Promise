package com.boosters.promise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.databinding.ItemSearchAddressResultBinding
import com.boosters.promise.network.LocalItemResponse

class SearchAddressListAdapter :
    ListAdapter<LocalItemResponse, SearchAddressListAdapter.SearchAddressViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAddressViewHolder {
        return SearchAddressViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SearchAddressViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    inner class SearchAddressViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_address_result, parent, false)
    ) {
        private val binding = ItemSearchAddressResultBinding.bind(itemView)

        fun bind(item: LocalItemResponse) {
            binding.textViewItemSearchAddressResultTitle.text = item.title
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<LocalItemResponse>() {
            override fun areItemsTheSame(
                oldItem: LocalItemResponse,
                newItem: LocalItemResponse
            ): Boolean {
                return oldItem.address == newItem.address
            }

            override fun areContentsTheSame(
                oldItem: LocalItemResponse,
                newItem: LocalItemResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}