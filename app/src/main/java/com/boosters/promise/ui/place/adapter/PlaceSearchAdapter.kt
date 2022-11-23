package com.boosters.promise.ui.place.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.boosters.promise.R
import com.boosters.promise.data.place.Place
import com.boosters.promise.databinding.ItemPlaceSearchResultBinding

class PlaceSearchAdapter(
    private var dataSet: List<Place> = List(SEARCH_RESULT_COUNT) { Place() },
    private val onClickListener: (item: Place) -> Unit,
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

    fun setDataSet(dataSet: List<Place>) {
        val filterList = dataSet + List(SEARCH_RESULT_COUNT) { Place() }
        this.dataSet = filterList.subList(0, SEARCH_RESULT_COUNT)
        notifyItemRangeChanged(0, SEARCH_RESULT_COUNT)
    }

    class PlaceSearchViewHolder(private val binding: ItemPlaceSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Place) {
            binding.textViewItemSearchAddressResultTitle.text = item.title
        }

    }

    companion object {
        const val SEARCH_RESULT_COUNT = 5
    }

}