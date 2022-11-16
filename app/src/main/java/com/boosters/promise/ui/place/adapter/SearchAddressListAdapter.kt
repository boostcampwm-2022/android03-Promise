package com.boosters.promise.ui.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.R
import com.boosters.promise.data.promise.Place
import com.boosters.promise.databinding.ItemSearchAddressResultBinding

class SearchAddressListAdapter(
    private val onClickListener: (item: Place) -> Unit
) : ListAdapter<Place, SearchAddressListAdapter.SearchAddressViewHolder>(diffUtil) {

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

        fun bind(item: Place) {
            binding.textViewItemSearchAddressResultTitle.text = item.placeTitle.parseAsHtml()
            itemView.setOnClickListener {
                onClickListener(item)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem.address == newItem.address
            }

            override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem == newItem
            }
        }
    }

}