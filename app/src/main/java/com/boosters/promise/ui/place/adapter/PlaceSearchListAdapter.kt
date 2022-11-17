package com.boosters.promise.ui.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.R
import com.boosters.promise.databinding.ItemSearchAddressResultBinding
import com.boosters.promise.ui.place.model.PlaceUiState

class PlaceSearchListAdapter(
    private val onClickListener: (item: PlaceUiState) -> Unit
) : ListAdapter<PlaceUiState, PlaceSearchListAdapter.SearchAddressViewHolder>(diffUtil) {

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

        fun bind(item: PlaceUiState) {
            binding.textViewItemSearchAddressResultTitle.text = item.title.parseAsHtml()
            itemView.setOnClickListener {
                onClickListener(item)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PlaceUiState>() {
            override fun areItemsTheSame(oldItem: PlaceUiState, newItem: PlaceUiState): Boolean {
                return oldItem.address == newItem.address
            }

            override fun areContentsTheSame(oldItem: PlaceUiState, newItem: PlaceUiState): Boolean {
                return oldItem == newItem
            }
        }
    }

}