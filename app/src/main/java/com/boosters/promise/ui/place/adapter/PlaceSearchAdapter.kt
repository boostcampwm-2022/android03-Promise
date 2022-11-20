package com.boosters.promise.ui.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.R
import com.boosters.promise.databinding.ItemSearchAddressResultBinding
import com.boosters.promise.ui.place.model.PlaceUiState

class PlaceSearchAdapter(
    private var dataSet: List<PlaceUiState> = List(SEARCH_RESULT_COUNT) { PlaceUiState() },
    private val onClickListener: (item: PlaceUiState) -> Unit
) : RecyclerView.Adapter<PlaceSearchAdapter.PlaceSearchViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlaceSearchViewHolder {
        return PlaceSearchViewHolder(viewGroup)
    }

    override fun onBindViewHolder(viewHolder: PlaceSearchViewHolder, position: Int) {
        if (dataSet.isNotEmpty()) {
            viewHolder.bind(dataSet[position])
            viewHolder.itemView.setOnClickListener {
                onClickListener(dataSet[position])
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun setDataSet(dataSet: List<PlaceUiState>) {
        val filterList = dataSet + List(SEARCH_RESULT_COUNT) { PlaceUiState() }
        this.dataSet = filterList.subList(0, SEARCH_RESULT_COUNT)
        notifyItemRangeChanged(0, SEARCH_RESULT_COUNT)
    }

    class PlaceSearchViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_address_result, parent, false)
    ) {
        private val binding = ItemSearchAddressResultBinding.bind(itemView)

        fun bind(item: PlaceUiState) {
            binding.textViewItemSearchAddressResultTitle.text = item.title
        }
    }

    companion object {
        const val SEARCH_RESULT_COUNT = 5
    }

}
