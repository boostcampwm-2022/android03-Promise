package com.boosters.promise.ui.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.R
import com.boosters.promise.databinding.ItemPlaceSearchResultBinding
import com.boosters.promise.ui.place.model.PlaceUiState

class PlaceSearchAdapter(
    private var dataSet: List<PlaceUiState> = List(SEARCH_RESULT_COUNT) { PlaceUiState() },
    private val onClickListener: (item: PlaceUiState) -> Unit,
) : RecyclerView.Adapter<PlaceSearchAdapter.PlaceSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceSearchViewHolder {
        val binding: ItemPlaceSearchResultBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_place_search_result,
            parent,
            false
        )

        return PlaceSearchViewHolder(binding).apply {
            itemView.setOnClickListener {
                onClickListener(dataSet[this.adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(viewHolder: PlaceSearchViewHolder, position: Int) {
        if (dataSet.isNotEmpty()) {
            viewHolder.bind(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size

    fun setDataSet(dataSet: List<PlaceUiState>) {
        val filterList = dataSet + List(SEARCH_RESULT_COUNT) { PlaceUiState() }
        this.dataSet = filterList.subList(0, SEARCH_RESULT_COUNT)
        notifyItemRangeChanged(0, SEARCH_RESULT_COUNT)
    }

    class PlaceSearchViewHolder(private val binding: ItemPlaceSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlaceUiState) {
            binding.textViewItemSearchAddressResultTitle.text = item.title
        }

    }

    companion object {
        const val SEARCH_RESULT_COUNT = 5
    }

}
